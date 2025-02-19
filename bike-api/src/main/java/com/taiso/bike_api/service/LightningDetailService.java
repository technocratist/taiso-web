package com.taiso.bike_api.service;

import com.taiso.bike_api.domain.LightningEntity;
import com.taiso.bike_api.dto.LightningDetailUpdateGetResponseDTO;
import com.taiso.bike_api.dto.LightningDetailUpdateRequestDTO;
import com.taiso.bike_api.dto.LightningDetailUpdateResponseDTO;
import com.taiso.bike_api.dto.RoutePostResponseDTO;
import com.taiso.bike_api.exception.LightningFullMemberException;
import com.taiso.bike_api.exception.LightningNotFoundException;
import com.taiso.bike_api.exception.NotPermissionException;
import com.taiso.bike_api.repository.LightningDetailRepository;
import com.taiso.bike_api.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class LightningDetailService {

    UserRepository userRepository;

    LightningDetailRepository lightningDetailRepository;

    // 번개 수정 화면에 기존 정보 뿌리기
    public LightningDetailUpdateGetResponseDTO getUpdateLightningDetail(Long lightningId,
                                         Authentication authentication) {
        if (lightningId == null || lightningId <= 0) {
            throw new NoSuchElementException(lightningId + "번개는 존재하지 않음");
        }

        // 사용자 ID 조회
        Long userId = userRepository.findByEmail(authentication.getName()).get().getUserId();
        // 해당 번개 조회
        Optional<LightningEntity> temp = lightningDetailRepository.findById(lightningId);

        if(!temp.isPresent()) {
            throw new LightningNotFoundException("존재하지 않는 번개 입니다.");
        }
        if (userId != temp.get().getCreatorId()) {
            throw new NotPermissionException("번개 생성자만 수정 할 수 있습니다.");
        }

        // Entity->DTO
        LightningEntity lightning = temp.get();
        LightningDetailUpdateGetResponseDTO lightningDetailUpdateGetResponseDTO = LightningDetailUpdateGetResponseDTO.builder()
                .lightningId(lightning.getLightningId())
                .creatorId(lightning.getCreatorId())
                .title(lightning.getTitle())
                .description(lightning.getDescription())
                .status(lightning.getStatus().toString())
                .gender(lightning.getGender().toString())
                .level(lightning.getLevel().toString())
                .recruitType(lightning.getRecruitType().toString())
                .bikeType(lightning.getBikeType().toString())
                .region(lightning.getRegion().toString())
//              .tagId(lightning.getTags().stream().toString())
                .build();

        return lightningDetailUpdateGetResponseDTO;

    }

    // 번개 디테일 업데이트
    public void updateLightningDetail(LightningDetailUpdateRequestDTO lightningDetailUpdateRequestDTO,
                                                                  Authentication authentication) {

        if (lightningDetailUpdateRequestDTO == null || lightningDetailUpdateRequestDTO.getLightningId() <= 0) {
            throw new LightningNotFoundException("존재하지 않는 번개 입니다.");
        }

        // 사용자 ID 조회
        Long userId = userRepository.findByEmail(authentication.getName()).get().getUserId();
        // DTO로 DB에서 해당 번개 조회
        Optional<LightningEntity> temp = lightningDetailRepository.findById(lightningDetailUpdateRequestDTO.getLightningId());

        if(!temp.isPresent()) {
            throw new LightningNotFoundException("존재하지 않는 번개 입니다.");
        }
        if (userId != temp.get().getCreatorId() || userId == null) {
            throw new NotPermissionException("번개 생성자만 수정 할 수 있습니다.");
        }

        LightningEntity entity = temp.get();

        if(!Objects.equals(entity.getStatus().toString(), "모집") || entity.getStatus() == null) {
            throw new LightningFullMemberException("번개가 이미 완료 되었거나, 취소 되어서 수정할 수 없습니다.");
        }

        // 업데이트
        entity.setTitle(lightningDetailUpdateRequestDTO.getTitle());
        entity.setDescription(lightningDetailUpdateRequestDTO.getDescription());
        entity.setStatus(LightningEntity.LightningStatus.valueOf(lightningDetailUpdateRequestDTO.getStatus().toString()));
        entity.setGender(LightningEntity.Gender.valueOf(lightningDetailUpdateRequestDTO.getGender().toString()));
        entity.setLevel(LightningEntity.Level.valueOf(lightningDetailUpdateRequestDTO.getLevel().toString()));
        entity.setRecruitType(LightningEntity.RecruitType.valueOf(lightningDetailUpdateRequestDTO.getRecruitType().toString()));;
        entity.setBikeType(LightningEntity.BikeType.valueOf(lightningDetailUpdateRequestDTO.getBikeType().toString()));;
        entity.setRegion(LightningEntity.Region.valueOf(lightningDetailUpdateRequestDTO.getRegion().toString()));;
    //    entity.setTags();

        // 확인
        log.info(entity.toString());
    }
}
