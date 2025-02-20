package com.taiso.bike_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.taiso.bike_api.domain.LightningEntity;
import com.taiso.bike_api.domain.LightningUserEntity;
import com.taiso.bike_api.domain.UserEntity;
import com.taiso.bike_api.exception.LightningNotFoundException;
import com.taiso.bike_api.exception.LightningUserAlreadyExistsException;
import com.taiso.bike_api.exception.UserNotFoundException;
import com.taiso.bike_api.repository.LightningRepository;
import com.taiso.bike_api.repository.LightningUserRepository;
import com.taiso.bike_api.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class LightningMemberService {
	
	@Autowired
	private LightningRepository lightningRepository;	
	
	@Autowired
	private LightningUserRepository lightningUserRepository;	
	
	@Autowired
	private UserRepository userRepository;
	
	
	// 번개 참가 서비스 
    @Transactional
	public void JoinParticipants(Long lightningId, Authentication authentication) {

    	// 예외처리 -> 404
    	LightningEntity lightningEntity = lightningRepository.findById(lightningId)
                .orElseThrow(() -> new LightningNotFoundException("번개를 찾을 수 없습니다."));
    	 	
        // 사용자 찾을 수 없음 -> 404
        UserEntity userEntity = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));    	

            // 중복 참여 체크
        lightningUserRepository.findByLightningAndUser(lightningEntity, userEntity)
            .ifPresent(existing -> {
                throw new LightningUserAlreadyExistsException("이미 참여한 번개입니다.");
            });
        
         // LightningUserEntity 생성 및 저장
        LightningUserEntity lightningUserEntity = LightningUserEntity.builder()
            .lightning(lightningEntity)
            .user(userEntity)
            .participantStatus(LightningUserEntity.ParticipantStatus.완료)
            .role(LightningUserEntity.Role.참여자)
            .build();
    	
        // 엔티티 저장
        lightningUserRepository.save(lightningUserEntity);    	
    	
	}

    // 인원 다 찼을 때 마감
    @Transactional
	public void autoClose(Long lightningId) {

    	// 예외처리 -> 404
    	LightningEntity lightningEntity = lightningRepository.findById(lightningId)
                .orElseThrow(() -> new LightningNotFoundException("번개를 찾을 수 없습니다."));		
    	
        // enum 값을 직접 할당
        lightningEntity.setStatus(LightningEntity.LightningStatus.마감);
	}
    

}
