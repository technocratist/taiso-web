package com.taiso.bike_api.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.taiso.bike_api.domain.RouteEntity;
import com.taiso.bike_api.domain.RoutePointEntity;
import com.taiso.bike_api.domain.RouteTagCategoryEntity;
import com.taiso.bike_api.dto.RouteRequestDTO;
import com.taiso.bike_api.repository.RoutePointRepository;
import com.taiso.bike_api.repository.RouteRepository;
import com.taiso.bike_api.repository.RouteTagCategoryRepository;

@Service
public class RouteService {

    @Autowired
    private RouteRepository routeRepository;
    
    @Autowired
    private RouteTagCategoryRepository routeTagCategoryRepository;

    @Autowired
    private RoutePointRepository routePointRepository;

    // 메타데이터(JSON)와 파일(Multipart/form-data)로부터 루트를 생성하는 메서드
    public RouteEntity createRoute(RouteRequestDTO dto, MultipartFile file) {

        // 파일 확장자 검증: gpx 또는 tcx 파일만 지원
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || 
            (!originalFilename.toLowerCase().endsWith(".gpx") && !originalFilename.toLowerCase().endsWith(".tcx"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "지원하지 않는 파일 타입");
        }

        // 파일 파싱: 경로 포인트, 총 거리, 고도 등 필요한 정보 추출
        GPXData gpxData;
        try {
            gpxData = parseGPXFile(file); // 실제 파싱 로직 구현
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "파일 파싱 중 오류가 발생하였습니다. 파일이 손상되었거나 올바른 형식이 아닙니다");
        }
        
        // 파일을 클라우드 버킷에 업로드 (시뮬레이션)
        Integer uploadedFileId = uploadFileToCloud(file);
        
        // 외부 API를 호출하여 정적 지도 이미지를 생성하고 클라우드에 저장 (시뮬레이션)
        Integer staticMapImgId = generateStaticMapImage(dto, gpxData);
        
        // 태그 문자열을 태그 엔티티로 처리 (기존 태그가 있으면 사용, 없으면 새로 생성)
        Set<RouteTagCategoryEntity> tags = dto.getTag().stream().map(tagName -> {
            return routeTagCategoryRepository.findByName(tagName)
                .orElseGet(() -> {
                    RouteTagCategoryEntity newTag = RouteTagCategoryEntity.builder().name(tagName).build();
                    return routeTagCategoryRepository.save(newTag);
                });
        }).collect(Collectors.toSet());
        
        // DTO의 필드와 파싱한 파일 데이터를 이용하여 RouteEntity 객체로 매핑
        RouteEntity route = RouteEntity.builder()
                .routeName(dto.getRouteName())
                .description(dto.getDescription())
                .userId(dto.getUserId())
                .region(convertRegion(dto.getRegion()))
                .distance(gpxData.getDistance())
                .altitude(gpxData.getAltitude())
                .distanceType(convertDistanceType(dto.getDistanceType()))
                .altitudeType(convertAltitudeType(dto.getAltitudeType()))
                .roadType(convertRoadType(dto.getRoadType()))
                .routeImgId(staticMapImgId)
                .tags(tags)
                .build();

        RouteEntity savedRoute = routeRepository.save(route);
        saveRoutePoints(savedRoute, gpxData.getRoutePoints());
        return savedRoute;
    }
    
    // 더미 구현: GPX/TCX 파일을 파싱하여 필요한 정보를 추출하는 메서드
    private GPXData parseGPXFile(MultipartFile file) throws Exception {
        String originalFilename = file.getOriginalFilename();
        boolean isGPX = originalFilename != null && originalFilename.toLowerCase().endsWith(".gpx");
        boolean isTCX = originalFilename != null && originalFilename.toLowerCase().endsWith(".tcx");
        
        List<GPXRoutePoint> points = new ArrayList<>();
        
        // XXE 공격을 방지하기 위한 안전한 XML 파싱 설정
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);
        
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file.getInputStream());
        doc.getDocumentElement().normalize();
        
        if (isGPX) {
            // GPX 트랙 포인트 파싱: <trkpt lat="..." lon="..."> 태그를 기대
            NodeList trkptList = doc.getElementsByTagName("trkpt");
            if (trkptList.getLength() == 0) {
                throw new Exception("GPX 파일에서 트랙 포인트를 찾을 수 없습니다.");
            }
            for (int i = 0; i < trkptList.getLength(); i++) {
                Node node = trkptList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element trkptElement = (Element) node;
                    String latStr = trkptElement.getAttribute("lat");
                    String lonStr = trkptElement.getAttribute("lon");
                    BigDecimal lat = new BigDecimal(latStr);
                    BigDecimal lon = new BigDecimal(lonStr);
                    
                    // 고도 정보 파싱 (존재할 경우)
                    BigDecimal elevation = BigDecimal.ZERO;
                    NodeList eleList = trkptElement.getElementsByTagName("ele");
                    if (eleList.getLength() > 0) {
                        Element eleElement = (Element) eleList.item(0);
                        String eleStr = eleElement.getTextContent();
                        elevation = new BigDecimal(eleStr);
                    }
                    
                    // 시간 정보 파싱 (존재할 경우)
                    LocalDateTime pointTime = LocalDateTime.now();
                    NodeList timeList = trkptElement.getElementsByTagName("time");
                    if (timeList.getLength() > 0) {
                        Element timeElement = (Element) timeList.item(0);
                        String timeStr = timeElement.getTextContent();
                        // ISO_LOCAL_DATE_TIME 형식을 가정하여, 끝에 'Z'가 있을 경우 제거 후 파싱
                        if (timeStr.endsWith("Z")) {
                            timeStr = timeStr.substring(0, timeStr.length() - 1);
                        }
                        pointTime = LocalDateTime.parse(timeStr);
                    }
                    
                    points.add(new GPXRoutePoint(lat, lon, elevation, pointTime));
                }
            }
        } else if (isTCX) {
            // TCX 파일의 경우, 유사한 파싱 로직을 구현해야 함
            throw new UnsupportedOperationException("TCX 파일 파싱은 구현되지 않았습니다.");
        } else {
            throw new Exception("지원하지 않는 파일 형식");
        }
        
        // 파싱된 경로 포인트를 이용하여 총 거리와 고도 상승량 계산
        BigDecimal totalDistance = BigDecimal.ZERO;
        BigDecimal totalAltitude = BigDecimal.ZERO;
        GPXRoutePoint prev = null;
        for (GPXRoutePoint point : points) {
            if (prev != null) {
                double lat1 = prev.getLatitude().doubleValue();
                double lon1 = prev.getLongitude().doubleValue();
                double lat2 = point.getLatitude().doubleValue();
                double lon2 = point.getLongitude().doubleValue();
                double earthRadius = 6371e3; // 미터 단위의 지구 반지름
                double dLat = Math.toRadians(lat2 - lat1);
                double dLon = Math.toRadians(lon2 - lon1);
                double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                           Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                           Math.sin(dLon / 2) * Math.sin(dLon / 2);
                double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                double distanceMeters = earthRadius * c;
                BigDecimal distanceKm = new BigDecimal(distanceMeters).divide(new BigDecimal(1000));
                totalDistance = totalDistance.add(distanceKm);
                
                // 이전 고도와 비교하여 양수 차이가 있을 때만 고도 상승량에 추가
                BigDecimal diff = point.getElevation().subtract(prev.getElevation());
                if (diff.compareTo(BigDecimal.ZERO) > 0) {
                    totalAltitude = totalAltitude.add(diff);
                }
            }
            prev = point;
        }
        
        return new GPXData(totalDistance, totalAltitude, points);
    }
    
    // 더미 메서드: 파일 업로드를 시뮬레이션하고 파일 식별자를 반환
    private Integer uploadFileToCloud(MultipartFile file) {
        // TODO: 클라우드 버킷으로 파일 업로드 구현
        return 1; // 더미 파일 ID
    }
    
    // 더미 메서드: 외부 API (예: 네이버 정적 지도 API)를 호출하여 정적 지도 이미지를 생성하고 클라우드에 저장 (시뮬레이션)
    private Integer generateStaticMapImage(RouteRequestDTO dto, GPXData gpxData) {
        // TODO: 외부 API 호출 및 정적 지도 이미지 클라우드 저장 구현
        return 1; // 더미 이미지 ID
    }
    
    // enum 타입 변환 도우미 메서드 (예시 매핑 로직 포함)
    private RouteEntity.Region convertRegion(String region) {
        try {
            return RouteEntity.Region.valueOf(region);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "지원하지 않는 지역");
        }
    }
    
    // 예시: 입력이 "단거리"이면 킬로미터, "장거리"이면 마일로 변환
    private RouteEntity.DistanceType convertDistanceType(String type) {
        return switch (type) {
            case "단거리" -> RouteEntity.DistanceType.킬로미터;
            case "장거리" -> RouteEntity.DistanceType.마일;
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "지원하지 않는 거리 유형");
        };
    }
    
    // 예시: 입력이 "평지"이면 미터, "고지"이면 피트로 변환
    private RouteEntity.AltitudeType convertAltitudeType(String type) {
        return switch (type) {
            case "평지" -> RouteEntity.AltitudeType.미터;
            case "고지" -> RouteEntity.AltitudeType.피트;
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "지원하지 않는 고도 유형");
        };
    }
    
    // 도로 유형에 대한 예시 변환: 예를 들어 "자전거 도로"는 평지로, "산길"은 산길, "고속도로"는 고속도로로 매핑
    private RouteEntity.RoadType convertRoadType(String type) {
        return switch (type) {
            case "자전거 도로" -> RouteEntity.RoadType.평지;
            case "산길" -> RouteEntity.RoadType.산길;
            case "고속도로" -> RouteEntity.RoadType.고속도로;
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "지원하지 않는 도로 유형");
        };
    }
    
    // 내부 더미 클래스: 파싱된 GPX 데이터를 저장하는 클래스
    private static class GPXData {
        private final BigDecimal distance;
        private final BigDecimal altitude;
        private final List<GPXRoutePoint> routePoints;
        
        public GPXData(BigDecimal distance, BigDecimal altitude, List<GPXRoutePoint> routePoints) {
            this.distance = distance;
            this.altitude = altitude;
            this.routePoints = routePoints;
        }
        
        public BigDecimal getDistance() {
            return distance;
        }
        
        public BigDecimal getAltitude() {
            return altitude;
        }
        
        public List<GPXRoutePoint> getRoutePoints() {
            return routePoints;
        }
    }

    // 내부 더미 클래스: 개별 GPX 경로 포인트를 표현하는 클래스
    private static class GPXRoutePoint {
        // 생성자에서만 할당되므로 final 처리
        private final BigDecimal latitude;
        private final BigDecimal longitude;
        private final BigDecimal elevation;
        
        public GPXRoutePoint(BigDecimal latitude, BigDecimal longitude, BigDecimal elevation, LocalDateTime time) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.elevation = elevation;
        }

        public BigDecimal getLatitude() {
            return latitude;
        }

        public BigDecimal getLongitude() {
            return longitude;
        }

        public BigDecimal getElevation() {
            return elevation;
        }
    }

    // 파싱된 경로 포인트들을 데이터베이스에 저장하는 메서드
    private void saveRoutePoints(RouteEntity route, List<GPXRoutePoint> points) {
        for (int i = 0; i < points.size(); i++) {
            GPXRoutePoint point = points.get(i);
            RoutePointEntity routePoint = RoutePointEntity.builder()
                    .route(route)
                    .latitude(point.getLatitude())
                    .longitude(point.getLongitude())
                    .elevation(point.getElevation())
                    .sequence(i) // 할당된 인덱스를 sequence 값으로 사용
                    .build();
            routePointRepository.save(routePoint);
        }
    }
}