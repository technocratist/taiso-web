package com.taiso.bike_api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.taiso.bike_api.domain.LightningEntity;
import com.taiso.bike_api.domain.LightningEntity.BikeType;
import com.taiso.bike_api.domain.LightningEntity.Gender;
import com.taiso.bike_api.domain.LightningEntity.Level;
import com.taiso.bike_api.domain.LightningEntity.Region;
import com.taiso.bike_api.domain.LightningTagCategoryEntity;
import com.taiso.bike_api.dto.LightningGetRequestDTO;
import com.taiso.bike_api.dto.LightningGetResponseDTO;
import com.taiso.bike_api.dto.LightningRequestDTO;
import com.taiso.bike_api.dto.LightningResponseDTO;
import com.taiso.bike_api.dto.ResponseComponentDTO;
import com.taiso.bike_api.repository.LightningRepository;
import com.taiso.bike_api.repository.LightningTagCategoryRepository;
import com.taiso.bike_api.repository.RouteRepository;
import com.taiso.bike_api.repository.UserRepository;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LightningService {
    
    @Autowired
    LightningRepository lightningRepository;

    @Autowired
    RouteRepository routeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LightningTagCategoryRepository lightningTagCategoryRepository;

    public LightningResponseDTO createLightning(LightningRequestDTO requestDTO, String userEmail) {
        // 태그 이름을 통해서 태그 엔티티 가져오기
        Set<LightningTagCategoryEntity> tags = requestDTO.getTags().stream()
            .map(tagName -> lightningTagCategoryRepository.findByName(tagName)
                    .orElseGet(() -> lightningTagCategoryRepository.save(
                            LightningTagCategoryEntity.builder().name(tagName).build())))
            .collect(Collectors.toSet());

        // 루트가 존재하는지 확인하는 예외처리 필요
        // non null 커럼에 null이 들어가는 경우의 예외처리
        // try{
            LightningEntity lightningEntity = LightningEntity.builder()
            .creatorId(userRepository.findByEmail(userEmail).get().getUserId())
            .title(requestDTO.getTitle())
            .description(requestDTO.getDescription())
            .eventDate(requestDTO.getEventDate())
            .duration(requestDTO.getDuration())
            .status(requestDTO.getStatus())
            .capacity(requestDTO.getCapacity())
            .latitude(requestDTO.getLatitude())
            .longitude(requestDTO.getLongitude())
            .gender(requestDTO.getGender())
            .level(requestDTO.getLevel())
            .recruitType(requestDTO.getRecruitType())
            .bikeType(requestDTO.getBikeType())
            .region(requestDTO.getRegion())
            .distance(requestDTO.getDistance())
            .route(routeRepository.findById(requestDTO.getRouteId()).get())
            .address(requestDTO.getAddress())
            .isClubOnly(requestDTO.getIsClubOnly())
            .clubId(requestDTO.getClubId())
            .tags(tags)
            .build();
        // } catch() {

        // }
        

        LightningEntity savedLightning = lightningRepository.save(lightningEntity);

        return LightningResponseDTO.builder().lightningId(savedLightning.getLightningId()).build();
    }

    public LightningGetResponseDTO getLightning(LightningGetRequestDTO requestDTO) {
        List<LightningEntity> entities = lightningRepository.findAll(filterBy(
                                                            requestDTO.getGender(), 
                                                            requestDTO.getLevel(), 
                                                            requestDTO.getBikeType(), 
                                                            requestDTO.getRegion(), 
                                                            requestDTO.getTags()));

        log.info("테스트트");
        LightningGetResponseDTO responseDTO = LightningGetResponseDTO.builder().lightnings(
            entities.stream().map(
                entity -> ResponseComponentDTO.builder()
                                            .lightningId(entity.getLightningId())
                                            .creatorId(entity.getCreatorId())
                                            .title(entity.getTitle())
                                            .eventDate(entity.getEventDate())
                                            .duration(entity.getDuration())
                                            .createdAt(entity.getCreatedAt())
                                            .status(entity.getStatus())
                                            .capacity(entity.getCapacity())
                                            .gender(entity.getGender())
                                            .level(entity.getLevel())
                                            .bikeType(entity.getBikeType())
                                            .tags(entity.getTags()
                                                    .stream()
                                                    .map(component -> component.getName())
                                                    .collect(Collectors.toList()))
                                            .address(entity.getAddress())
                                            .routeImgId(entity.getRoute().getRouteImgId())
                                            .build())
                                            .collect(Collectors.toList()))
                                            .build();

        return responseDTO;
    }

    // 가져올 번개리스트를 필터링하기 위한 필터를 반환하는 메서드
    // gender값이 null인 경우는 자동으로 필터링 기준에서 제외 하는 방식
    public static Specification<LightningEntity> filterBy(
        Gender gender, Level level, BikeType bikeType, Region region, List<String> tags
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (gender != null) {
                predicates.add(criteriaBuilder.equal(root.get("gender"), gender));
            }
            if (level != null) {
                predicates.add(criteriaBuilder.equal(root.get("level"), level));
            }
            if (bikeType != null) {
                predicates.add(criteriaBuilder.equal(root.get("bikeType"), bikeType));
            }
            if (region != null) {
                predicates.add(criteriaBuilder.equal(root.get("region"), region));
            }
            if (tags != null && !tags.isEmpty()) {
                Join<LightningEntity, LightningTagCategoryEntity> tagJoin = root.join("tags");
                predicates.add(tagJoin.get("tagName").in(tags));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
