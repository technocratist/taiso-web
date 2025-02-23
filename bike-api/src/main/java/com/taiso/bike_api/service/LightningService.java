package com.taiso.bike_api.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taiso.bike_api.domain.LightningEntity;
import com.taiso.bike_api.domain.LightningTagCategoryEntity;
import com.taiso.bike_api.dto.GetParticipatedLightningResponseDTO;
import com.taiso.bike_api.dto.LightningRequestDTO;
import com.taiso.bike_api.dto.LightningResponseDTO;
import com.taiso.bike_api.repository.LightningRepository;
import com.taiso.bike_api.repository.LightningTagCategoryRepository;
import com.taiso.bike_api.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
            .routeId(requestDTO.getRouteId())
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

    
    
    
    
    
    public GetParticipatedLightningResponseDTO getParticipatedLightnings(Long lightningId, String userEmail) {
        // 사용자가 해당 번개에 정말 참여한 사람인지 확인
        lightningUserRepository.findByLightningIdAndUserId(lightningId, userRepository.findByEmail(userEmail).get().getUserId())
            .orElseThrow(() -> new IllegalArgumentException("해당 번개에 참여한 사용자가 아닙니다."));

        // 해당 번개 정보 조회

        // 사용자가 해당 번개 내에서 북마크한 사람 조회

        // 조회된 사람들의 상세정보 조회


    }
}
