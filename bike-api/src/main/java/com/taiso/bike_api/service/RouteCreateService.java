package com.taiso.bike_api.service;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.CoordinateTransform;
import org.locationtech.proj4j.CoordinateTransformFactory;
import org.locationtech.proj4j.ProjCoordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.taiso.bike_api.domain.RouteEntity;
import com.taiso.bike_api.domain.RoutePointEntity;
import com.taiso.bike_api.domain.RouteTagCategoryEntity;
import com.taiso.bike_api.dto.RouteRequestDTO;
import com.taiso.bike_api.exception.InvalidFileExtensionException;
import com.taiso.bike_api.exception.StaticMapImageFetchException;
import com.taiso.bike_api.exception.UnsupportedEnumException;
import com.taiso.bike_api.repository.RoutePointRepository;
import com.taiso.bike_api.repository.RouteRepository;
import com.taiso.bike_api.repository.RouteTagCategoryRepository;



@Service
public class RouteCreateService {

    @Autowired
    private RouteRepository routeRepository;
    
    @Autowired
    private RouteTagCategoryRepository routeTagCategoryRepository;

    @Autowired
    private RoutePointRepository routePointRepository;

    @Value("${naver.api.key.id}")
    private String naverApiKeyId;
    @Value("${naver.api.key.key}")
    private String naverApiKey;


    // 메타데이터(JSON)와 파일(Multipart/form-data)로부터 루트를 생성하는 메서드
    public RouteEntity createRoute(RouteRequestDTO dto, MultipartFile file) {
        // 실행 시간 측정을 시작
        long startTime = System.currentTimeMillis();
        
        // 파일 확장자 검증: gpx 또는 tcx 파일만 지원
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || 
            (!originalFilename.toLowerCase().endsWith(".gpx") && !originalFilename.toLowerCase().endsWith(".tcx"))) {
            throw new InvalidFileExtensionException("지원하지 않는 파일 타입");
        }

        // 파일 파싱: 경로 포인트, 총 거리, 고도 등 필요한 정보 추출
        GPXData gpxData;
        try {
            gpxData = parseGPXFile(file); // 실제 파싱 로직 구현
        } catch(Exception e) {
            throw new InvalidFileExtensionException("파일 파싱 중 오류가 발생하였습니다. 파일이 손상되었거나 올바른 형식이 아닙니다");
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
        
        // 실행 시간 측정 종료 및 출력
        long endTime = System.currentTimeMillis();
        System.out.println("--------------------------------");
        System.out.println("createRoute 실행 시간: " + (endTime - startTime) + " ms");
        System.out.println("--------------------------------");
        
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
                throw new InvalidFileExtensionException("GPX 파일에서 트랙 포인트를 찾을 수 없습니다.");
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
            throw new InvalidFileExtensionException("TCX 파일 파싱은 구현되지 않았습니다.");
        } else {
            throw new InvalidFileExtensionException("지원하지 않는 파일 형식");
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
    
    /**
     * 네이버 정적 지도 API로부터 지도 이미지를 받아 GPX 경로를 합성한 후,
     * 로컬 파일 시스템에 저장하고 결과 ID(더미)를 반환합니다.
     */
    Integer generateStaticMapImage(RouteRequestDTO dto, GPXData gpxData) {
        try {
            // 1. base 지도 이미지 크기 및 scaleFactor 지정
            int baseWidth = 800;
            int baseHeight = 600;
            int scaleFactor = 2; // 예: 고해상도 이미지 사용 (scale=2)

            // 2. GPX 경로의 모든 포인트에서 바운딩 박스 계산
            List<GPXRoutePoint> points = gpxData.getRoutePoints();
            if (points.isEmpty()) {
                throw new InvalidFileExtensionException("파일에 경로 포인트가 없습니다.");
            }
            double minLat = Double.MAX_VALUE, maxLat = -Double.MAX_VALUE;
            double minLng = Double.MAX_VALUE, maxLng = -Double.MAX_VALUE;
            for (GPXRoutePoint pt : points) {
                double lat = pt.getLatitude().doubleValue();
                double lng = pt.getLongitude().doubleValue();
                if (lat < minLat) minLat = lat;
                if (lat > maxLat) maxLat = lat;
                if (lng < minLng) minLng = lng;
                if (lng > maxLng) maxLng = lng;
            }

            // 3. 바운딩 박스의 중심 좌표 계산
            double centerLat = (minLat + maxLat) / 2.0;
            double centerLng = (minLng + maxLng) / 2.0;

            // 4. 바운딩 박스에 맞춰 적절한 zoom 레벨 계산 (baseWidth, baseHeight 사용)
            int zoom = computeZoom(minLat, minLng, maxLat, maxLng, baseWidth, baseHeight) - 1;

            // 5. 네이버 정적 지도 API URL 구성 (base 크기와 scaleFactor 사용)
            String url = "https://naveropenapi.apigw.ntruss.com/map-static/v2/raster" +
                         "?w=" + baseWidth +
                         "&h=" + baseHeight +
                         "&center=" + centerLng + "," + centerLat +
                         "&level=" + zoom +
                         "&maptype=basic" +
                         "&format=png" +
                         "&scale=" + scaleFactor;

            // 6. RestTemplate 및 헤더 설정 (API 키는 실제 값으로 대체)
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("x-ncp-apigw-api-key-id", naverApiKeyId);
            headers.set("x-ncp-apigw-api-key", naverApiKey);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class);
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new StaticMapImageFetchException("정적 지도 이미지를 가져오지 못했습니다. (상태 코드: " + response.getStatusCode() + ")");
            }
            byte[] imageBytes = response.getBody();
            if (imageBytes == null) {
                throw new StaticMapImageFetchException("받은 이미지 데이터가 없습니다.");
            }

            // 7. 응답받은 byte[] 데이터를 BufferedImage로 변환
            BufferedImage baseMapImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
            // 실제 이미지의 픽셀 크기 (scaleFactor 적용됨)
            int actualWidth = baseMapImage.getWidth();
            int actualHeight = baseMapImage.getHeight();
            int centerX = actualWidth / 2;
            int centerY = actualHeight / 2;

            // 8. 오버레이를 위한 좌표 계산
            // convertGeoToPixel()는 static map의 줌(zoom)과 scaleFactor로 계산
            // 이후, 지도 중앙 기준으로 오프셋에 alpha (< 1) 배율을 적용하여 더 넓은 영역을 표현함
            double alpha = 2; // 1단계 낮은 줌 효과: (1/2)배. 필요시 값을 조정하세요.

            int n = points.size();
            int[] xPoints = new int[n];
            int[] yPoints = new int[n];
            for (int i = 0; i < n; i++) {
                GPXRoutePoint pt = points.get(i);
                java.awt.Point pixel = convertGeoToPixel(
                        pt.getLatitude().doubleValue(),
                        pt.getLongitude().doubleValue(),
                        centerLat, centerLng,
                        actualWidth, actualHeight, zoom, scaleFactor
                );
                // 지도 중앙을 기준으로 오프셋에 alpha를 곱해 축소
                int adjustedX = centerX + (int) Math.round((pixel.x - centerX) * alpha);
                int adjustedY = centerY + (int) Math.round((pixel.y - centerY) * alpha);
                xPoints[i] = adjustedX;
                yPoints[i] = adjustedY;
            }

            Graphics2D g2d = baseMapImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setStroke(new BasicStroke(5));
            g2d.setColor(Color.RED);
            g2d.drawPolyline(xPoints, yPoints, n);
            g2d.dispose();

            // 9. 합성된 이미지를 바이트 배열로 변환 후 로컬에 저장
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(baseMapImage, "png", baos);
            byte[] compositeImageBytes = baos.toByteArray();

            return uploadCompositeImage(compositeImageBytes);
        } catch (IOException | RestClientException e) {
            throw new StaticMapImageFetchException("정적 지도 이미지 생성 중 오류: " + e.getMessage());
        }
    }

    /**
     * GPX 경로의 바운딩 박스를 기반으로, Web Mercator 방식을 활용해 적절한 zoom 레벨을 계산합니다.
     */
    private int computeZoom(double minLat, double minLng, double maxLat, double maxLng, int imageWidth, int imageHeight) {
        double tileSize = 256;

        // 경도 차이 계산 (국제 날짜 변경선 보정)
        double lngDelta = Math.abs(maxLng - minLng);
        if (lngDelta > 180) {
            lngDelta = 360 - lngDelta;
        }
        double diffX = lngDelta / 360.0;

        // 위도는 Web Mercator 변환 적용
        double latRadMin = Math.toRadians(minLat);
        double latRadMax = Math.toRadians(maxLat);
        double minY = (1 - Math.log(Math.tan(latRadMin) + 1 / Math.cos(latRadMin)) / Math.PI) / 2;
        double maxY = (1 - Math.log(Math.tan(latRadMax) + 1 / Math.cos(latRadMax)) / Math.PI) / 2;
        double diffY = Math.abs(maxY - minY);

        if (diffX == 0 || diffY == 0) {
            return 16;
        }

        double zoomX = Math.log(imageWidth / (tileSize * diffX)) / Math.log(2);
        double zoomY = Math.log(imageHeight / (tileSize * diffY)) / Math.log(2);

        int zoom = (int) Math.floor(Math.min(zoomX, zoomY));
        zoom = Math.max(0, Math.min(zoom, 19)) ;
        return zoom;
    }

    /**
     * Proj4j를 사용하여 WGS84(EPSG:4326) 좌표를 Web Mercator(EPSG:3857) 좌표로 변환한 후,
     * 지도 이미지 내의 픽셀 좌표로 환산합니다.
     * imageWidth와 imageHeight는 실제 반환된 이미지의 픽셀 크기이고,
     * zoom과 scaleFactor는 static map API 요청 시 사용한 값입니다.
     */
    private java.awt.Point convertGeoToPixel(double lat, double lng, double centerLat, double centerLng,
                                               int imageWidth, int imageHeight, int zoom, int scaleFactor) {
        CRSFactory crsFactory = new CRSFactory();
        CoordinateReferenceSystem crsWGS84 = crsFactory.createFromName("EPSG:4326");
        CoordinateReferenceSystem crs3857 = crsFactory.createFromName("EPSG:3857");

        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
        CoordinateTransform transform = ctFactory.createTransform(crsWGS84, crs3857);

        ProjCoordinate srcCoord = new ProjCoordinate(lng, lat);
        ProjCoordinate dstCoord = new ProjCoordinate();
        transform.transform(srcCoord, dstCoord);

        ProjCoordinate srcCenter = new ProjCoordinate(centerLng, centerLat);
        ProjCoordinate dstCenter = new ProjCoordinate();
        transform.transform(srcCenter, dstCenter);

        // 해상도 계산: zoom 0에서의 해상도 약 156543.03392804062 m/px, scaleFactor 반영
        double resolution = (156543.03392804062 / Math.pow(2, zoom)) / scaleFactor;

        int pixelX = (int) Math.round(imageWidth / 2.0 + (dstCoord.x - dstCenter.x) / resolution);
        int pixelY = (int) Math.round(imageHeight / 2.0 - (dstCoord.y - dstCenter.y) / resolution);

        return new java.awt.Point(pixelX, pixelY);
    }
    /**
     * 합성된 이미지를 로컬 파일 시스템에 저장하는 더미 메서드.
     */
    private Integer uploadCompositeImage(byte[] imageBytes) {
        try {
            // 저장할 디렉토리 (없으면 생성)
            File outputDir = new File("saved_images");
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            // 파일 이름 생성 (타임스탬프 기반)
            String filename = "saved_images/composite_" + System.currentTimeMillis() + ".png";
            File outputFile = new File(filename);

            // 파일에 이미지 데이터 저장
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                fos.write(imageBytes);
            }

            System.out.println("Composite image saved to: " + outputFile.getAbsolutePath());

            // 실제 시스템에서는 파일 경로나 ID를 반환할 수 있음. 여기서는 더미 값 2 반환.
            return 2;
        } catch (IOException e) {
            throw new StaticMapImageFetchException("이미지 로컬 저장 중 오류: " + e.getMessage());
        }
    }
    
    // enum 타입 변환 도우미 메서드 (예시 매핑 로직 포함)
    private RouteEntity.Region convertRegion(String region) {
        try {
            return RouteEntity.Region.valueOf(region);
        } catch (IllegalArgumentException e) {
            throw new UnsupportedEnumException("지원하지 않는 지역 유형");
        }
    }
    
    // 예시: 입력이 "단거리"이면 킬로미터, "장거리"이면 마일로 변환
    private RouteEntity.DistanceType convertDistanceType(String type) {
        return switch (type) {
            case "단거리" -> RouteEntity.DistanceType.킬로미터;
            case "장거리" -> RouteEntity.DistanceType.마일;
            default -> throw new UnsupportedEnumException("지원하지 않는 거리 유형");
        };
    }
    
    // 예시: 입력이 "평지"이면 미터, "고지"이면 피트로 변환
    private RouteEntity.AltitudeType convertAltitudeType(String type) {
        return switch (type) {
            case "평지" -> RouteEntity.AltitudeType.미터;
            case "고지" -> RouteEntity.AltitudeType.피트;
            default -> throw new UnsupportedEnumException("지원하지 않는 고도 유형");
        };
    }
    
    // 도로 유형에 대한 예시 변환: 예를 들어 "자전거 도로"는 평지로, "산길"은 산길, "고속도로"는 고속도로로 매핑
    private RouteEntity.RoadType convertRoadType(String type) {
        return switch (type) {
            case "자전거 도로" -> RouteEntity.RoadType.평지;
            case "산길" -> RouteEntity.RoadType.산길;
            case "고속도로" -> RouteEntity.RoadType.고속도로;
            default -> throw new UnsupportedEnumException("지원하지 않는 도로 유형");
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
        List<RoutePointEntity> entities = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            GPXRoutePoint point = points.get(i);
            RoutePointEntity routePoint = RoutePointEntity.builder()
                    .route(route)
                    .latitude(point.getLatitude())
                    .longitude(point.getLongitude())
                    .elevation(point.getElevation())
                    .sequence(i)
                    .build();
            entities.add(routePoint);
        }
        routePointRepository.saveAll(entities);
    }
}