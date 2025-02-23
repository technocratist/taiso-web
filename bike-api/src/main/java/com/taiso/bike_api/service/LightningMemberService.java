package com.taiso.bike_api.service;

import java.util.Optional;

import com.taiso.bike_api.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.taiso.bike_api.domain.LightningEntity;
import com.taiso.bike_api.domain.LightningEntity.LightningStatus;
import com.taiso.bike_api.domain.LightningEntity.RecruitType;
import com.taiso.bike_api.domain.LightningUserEntity;
import com.taiso.bike_api.domain.UserEntity;
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

    	// 번개 아이디로 엔티티 가져오기 
    	LightningEntity lightningEntity = lightningRepository.findById(lightningId)
    			// 예외처리 -> 404
                .orElseThrow(() -> new LightningNotFoundException("번개를 찾을 수 없습니다."));
    	 	
        // 유저 아이디로 엔티티 가져오기
        UserEntity userEntity = userRepository.findByEmail(authentication.getName())
        		// 사용자 찾을 수 없음 -> 404
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));    	

        // 중복 참여 체크: 이미 참여한 내역이 있으면 예외 발생
        Optional<LightningUserEntity> existingParticipation = lightningUserRepository
                .findByLightningAndUser(lightningEntity, userEntity);
        if (existingParticipation.isPresent()) {
            throw new LightningUserAlreadyExistsException("이미 참여한 번개입니다.");
        }
        

    	// 번개의 상태 모집, 마감, 종료, 취소
    	if(lightningEntity.getStatus() == LightningStatus.모집) {
    		// 번개의 타입 - 참가형, 수락형
    		if(lightningEntity.getRecruitType() == RecruitType.참가형) {
        		
                // LightningUserEntity 생성 시 기본값 할당
                LightningUserEntity lightningUserEntity = LightningUserEntity.builder()
                        .lightning(lightningEntity)
                        .user(userEntity)
                        .participantStatus(LightningUserEntity.ParticipantStatus.완료) // 기본 상태
                        .role(LightningUserEntity.Role.참여자) // 기본 역할
                        .build();    	
                
             // 엔티티 저장
                try {
             	   lightningUserRepository.save(lightningUserEntity);  
                } catch (DataIntegrityViolationException e) {
                    throw new EmailAlreadyExistsException("무결성 제약 조건이 위배 저장 중 오류가 발생했습니다.");
                }
    			
            // 번개의 타입이 수락형인 경우
    		}else if(lightningEntity.getRecruitType() == RecruitType.수락형 ) {
                // LightningUserEntity 생성 시 기본값 할당
                LightningUserEntity lightningUserEntity = LightningUserEntity.builder()
                        .lightning(lightningEntity)
                        .user(userEntity)
                        .participantStatus(LightningUserEntity.ParticipantStatus.신청대기) // 기본 상태
                        .role(LightningUserEntity.Role.참여자) // 기본 역할
                        .build(); 
                
             // 엔티티 저장 트라이 케치 -> 유니크 
               try {
            	   lightningUserRepository.save(lightningUserEntity);  
               } catch (DataIntegrityViolationException e) {
                   throw new EmailAlreadyExistsException("무결성 제약 조건이 위배 저장 중 오류가 발생했습니다.");
               }
    		}
    		
    		// 400
    	}else if(lightningEntity.getStatus() == LightningStatus.마감) {
    		throw new LightningStatusMismatchException("마감된 번개에 참여할 수 없습니다.");
    		
    	}else if(lightningEntity.getStatus() == LightningStatus.종료) {
    		throw new LightningStatusMismatchException("종료된 번개에 참여할 수 없습니다.");
    		
    	}else if(lightningEntity.getStatus() == LightningStatus.취소) {
    		throw new LightningStatusMismatchException("취소된 번개에 참여할 수 없습니다.");
    	}

	}

    // 인원 다 찼을 때 마감
    @Transactional
	public void autoClose(Long lightningId) {

    	// 번개 아이디로 엔티티 가져오기
    	LightningEntity lightningEntity = lightningRepository.findById(lightningId)
    			//예외처리 -> 404
                .orElseThrow(() -> new LightningNotFoundException("번개를 찾을 수 없습니다."));		
    	
        // enum 값을 직접 할당
        lightningEntity.setStatus(LightningEntity.LightningStatus.마감);
	}

    // 번개 강제 마감
    @Transactional
	public void lightningClose(Long lightningId, Authentication authentication) {

    	// 번개 아이디로 엔티티 가져오기
    	LightningEntity lightningEntity = lightningRepository.findById(lightningId)
    			// 예외처리 -> 404
                .orElseThrow(() -> new LightningNotFoundException("번개를 찾을 수 없습니다."));		
    	
    	// 유저 아이디로 엔티티 가져오기
    	UserEntity userEntity = userRepository.findByEmail(authentication.getName()).get();    	
    	
    	// 유저 아이디와 생성자 불일치 권한 없음 -> 403 FORBIDDEN
    	if(userEntity.getUserId() != lightningEntity.getCreatorId()) {
    		throw new LightningCreatorMismatchException("유저와 번개 생성자가 같지 않음");
    	}
    	
    	// enum 값을 직접 할당

        lightningEntity.setStatus(LightningEntity.LightningStatus.마감);
		
	}

	// 스스로 번개 나가기
	@Transactional
	public void exitLightning(Long lightningId, Authentication authentication) {
		// 번개 존재 확인
		LightningEntity lightningEntity = lightningRepository.findById(lightningId)
				// 예외처리 -> 404
				.orElseThrow(() -> new LightningNotFoundException("번개를 찾을 수 없습니다."));

		// 유저 아이디로 유저 찾기
		UserEntity userEntity = userRepository.findByEmail(authentication.getName())
				.orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

		// 번개Id와 유저Id로 member에서 조회
		LightningUserEntity lightningUser = lightningUserRepository
				.findByLightning_LightningIdAndUser_UserId(lightningId, userEntity.getUserId())
				.orElseThrow(() -> new LightningMemberNotFoundException("해당 번개에 참여한 기록이 없습니다."));

		// 이미 탈퇴한 경우 예외 처리
		if (lightningUser.getParticipantStatus() == LightningUserEntity.ParticipantStatus.탈퇴) {
			throw new LightningMemberIllegalParticipantStatusException("이미 탈퇴한 회원입니다.");
		}

		// 상태를 '탈퇴'로 변경
		lightningUser.setParticipantStatus(LightningUserEntity.ParticipantStatus.탈퇴);
		lightningUserRepository.save(lightningUser);

	}

	// 번개 참여신청 취소하기
	public void cancelJoinLightning(Long lightningId, Authentication authentication) {

		// 번개 존재 확인
		LightningEntity lightningEntity = lightningRepository.findById(lightningId)
				// 예외처리 -> 404
				.orElseThrow(() -> new LightningNotFoundException("번개를 찾을 수 없습니다."));

		// 유저 아이디로 유저 찾기
		UserEntity userEntity = userRepository.findByEmail(authentication.getName())
				.orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

		// 번개Id와 유저Id로 member에서 조회
		LightningUserEntity lightningUser = lightningUserRepository
				.findByLightning_LightningIdAndUser_UserId(lightningId, userEntity.getUserId())
				.orElseThrow(() -> new LightningMemberNotFoundException("해당 번개에 참여한 기록이 없습니다."));

		// 이미 탈퇴한 경우 예외 처리
		if (lightningUser.getParticipantStatus() == LightningUserEntity.ParticipantStatus.탈퇴) {
			throw new LightningMemberIllegalParticipantStatusException("이미 탈퇴된 회원입니다.");
		}

		// 이미 승인된 경우 예외 처리
		if (lightningUser.getParticipantStatus() == LightningUserEntity.ParticipantStatus.승인) {
			throw new LightningMemberIllegalParticipantStatusException("이미 승인된 회원입니다.");
		}

		// 신청대기 상태일 때만 작동
		if (lightningUser.getParticipantStatus() == LightningUserEntity.ParticipantStatus.신청대기) {
			lightningUser.setParticipantStatus(LightningUserEntity.ParticipantStatus.탈퇴);
			lightningUserRepository.save(lightningUser);
		}

	}
}
