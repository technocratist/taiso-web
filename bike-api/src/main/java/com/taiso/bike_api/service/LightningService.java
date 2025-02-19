package com.taiso.bike_api.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taiso.bike_api.domain.LightningEntity;
import com.taiso.bike_api.domain.LightningTagCategoryEntity;
import com.taiso.bike_api.dto.LightningRequestDTO;
import com.taiso.bike_api.dto.LightningResponseDTO;
import com.taiso.bike_api.repository.LightningRepository;
import com.taiso.bike_api.repository.LightningTagCategoryRepository;
import com.taiso.bike_api.repository.UserRepository;

@Service
public class LightningService {
    
    @Autowired
    LightningRepository lightningRepository;

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

        // 번개 엔티티 빌드
        LightningEntity lightningEntity = LightningEntity.builder()
                                                        .creatorId(userRepository.getUserIdByEmail(userEmail))
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
                                                        .routeId(requestDTO.getRouteId())
                                                        .address(requestDTO.getAddress())
                                                        .isClubOnly(requestDTO.getIsClubOnly())
                                                        .clubId(requestDTO.getClubId())
                                                        .tags(tags)
                                                        .build();

        LightningEntity savedLightning = lightningRepository.save(lightningEntity);

        return LightningResponseDTO.builder().lightningId(savedLightning.getLightningId()).build();
    }
}
