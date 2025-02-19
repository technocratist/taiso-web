package com.taiso.bike_api.service;

import java.util.Optional;

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
    	LightningEntity lightningEntityException = lightningRepository.findById(lightningId)
                .orElseThrow(() -> new LightningNotFoundException("번개를 찾을 수 없습니다."));
    	 	
        // 사용자 찾을 수 없음 -> 404
        UserEntity userEntityException = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));    	

        // 중복 참여 체크: 이미 참여한 내역이 있으면 예외 발생
        Optional<LightningUserEntity> existingParticipation = lightningUserRepository
                .findByLightningAndUser(lightningEntityException, userEntityException);
        if (existingParticipation.isPresent()) {
            throw new LightningUserAlreadyExistsException("이미 참여한 번개입니다.");
        }
        
        
    	
    	// 번개 아이디로 엔티티 가져오기
    	LightningEntity lightningEntity = lightningRepository.findById(lightningId).get();
    	
    	// 유저 아이디로 엔티티 가져오기
    	UserEntity userEntity = userRepository.findByEmail(authentication.getName()).get();

        // LightningUserEntity 생성 시 기본값 할당
        LightningUserEntity lightningUserEntity = LightningUserEntity.builder()
                .lightning(lightningEntity)
                .user(userEntity)
                .participantStatus(LightningUserEntity.ParticipantStatus.완료) // 기본 상태
                .role(LightningUserEntity.Role.참여자) // 기본 역할
                .build();    	
    	
        // 엔티티 저장
        lightningUserRepository.save(lightningUserEntity);    	
    	
	}

    // 인원 다 찼을 때 마감
    @Transactional
	public void autoClose(Long lightningId) {

    	// 예외처리 -> 404
    	LightningEntity lightningEntityException = lightningRepository.findById(lightningId)
                .orElseThrow(() -> new LightningNotFoundException("번개를 찾을 수 없습니다."));		
    	
    	// 번개 아이디로 엔티티 가져오기
    	LightningEntity lightningEntity = lightningRepository.findById(lightningId).get();
    	
        // enum 값을 직접 할당
        lightningEntity.setStatus(LightningEntity.LightningStatus.마감);
	}
    
    

}
