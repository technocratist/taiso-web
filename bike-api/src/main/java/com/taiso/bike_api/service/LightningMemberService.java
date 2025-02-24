package com.taiso.bike_api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.taiso.bike_api.domain.LightningEntity;
import com.taiso.bike_api.domain.LightningEntity.LightningStatus;
import com.taiso.bike_api.domain.LightningEntity.RecruitType;
import com.taiso.bike_api.domain.LightningUserEntity;
import com.taiso.bike_api.domain.LightningUserEntity.ParticipantStatus;
import com.taiso.bike_api.domain.UserEntity;
import com.taiso.bike_api.exception.EmailAlreadyExistsException;
import com.taiso.bike_api.exception.LightningCreatorMismatchException;
import com.taiso.bike_api.exception.LightningNotFoundException;
import com.taiso.bike_api.exception.LightningStatusMismatchException;
import com.taiso.bike_api.exception.LightningUserAlreadyExistsException;
import com.taiso.bike_api.exception.LightningUserMismatchException;
import com.taiso.bike_api.exception.LightningUserStatusNotPendingException;
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

    // 번개 참가 수락 -> 번개 아이디, 참가 신청 아이디, 관리자 아이디
    @Transactional
	public void JoinRequests(Long lightningId, Long userId, Authentication authentication) {

		// 번개 아이디로 엔티티 가져오기
    	LightningEntity lightningEntity = lightningRepository.findById(lightningId)
    			// 예외처리 -> 404
                .orElseThrow(() -> new LightningNotFoundException("번개를 찾을 수 없습니다."));	
    	
    	// 유저 아이디로 엔티티 가져오기
        UserEntity userEntity = userRepository.findByEmail(authentication.getName())
        		// 사용자 찾을 수 없음 -> 404
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        LightningUserEntity joinUserEntity = lightningUserRepository.findById(userId)
				// 사용자 찾을 수 없음 -> 404
		        .orElseThrow(() -> new UserNotFoundException("참가 사용자를 찾을 수 없습니다."));
        
        // 번개와 참가 신청하는 유저가 일치하지 않음
//        LightningUserEntity lightningUser = lightningUserRepository.findByLightningAndUser(lightningEntity, userEntity)
//                .orElseThrow(() -> new LightningUserMismatchException("번개와 참가 신청하는 유저가 일치하지 않음"));
       
        if (!joinUserEntity.getLightning().getLightningId().equals(lightningEntity.getLightningId())) {
            throw new LightningUserMismatchException("번개와 참가 신청하는 유저가 일치하지 않음");
        }
        
	     // 유저 아이디와 생성자 불일치 권한 없음 -> 403 FORBIDDEN
    	if(userEntity.getUserId() != lightningEntity.getCreatorId()) {
    		throw new LightningCreatorMismatchException("유저와 번개 생성자가 같지 않음");
    	}
	   
    	// 유저가 신청대기 상태가 아닌 경우
    	if( !(joinUserEntity.getParticipantStatus() == ParticipantStatus.신청대기)) {
    		throw new LightningUserStatusNotPendingException("신청대기 상태가 아닌 경우");
    	}
    	
    	// 승인 처리
        joinUserEntity.setParticipantStatus(LightningUserEntity.ParticipantStatus.승인);
	}

    // 번개 참가 거절 (탈퇴)
    @Transactional
	public void JoinRejection(Long lightningId, Long userId, Authentication authentication) {

    	// 번개 아이디로 엔티티 가져오기
    	LightningEntity lightningEntity = lightningRepository.findById(lightningId)
    			// 예외처리 -> 404
                .orElseThrow(() -> new LightningNotFoundException("번개를 찾을 수 없습니다."));	
    	
    	// 유저 아이디로 엔티티 가져오기
        UserEntity userEntity = userRepository.findByEmail(authentication.getName())
        		// 사용자 찾을 수 없음 -> 404
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        LightningUserEntity joinUserEntity = lightningUserRepository.findById(userId)
				// 사용자 찾을 수 없음 -> 404
		        .orElseThrow(() -> new UserNotFoundException("참가 사용자를 찾을 수 없습니다."));
        
        // 번개와 참가 신청하는 유저가 일치하지 않음
        LightningUserEntity lightningUser = lightningUserRepository.findByLightningAndUser(lightningEntity, userEntity)
                .orElseThrow(() -> new LightningCreatorMismatchException("번개와 참가 신청하는 유저가 일치하지 않음"));
       
        
	     // 유저 아이디와 생성자 불일치 권한 없음 -> 403 FORBIDDEN
    	if(userEntity.getUserId() != lightningEntity.getCreatorId()) {
    		throw new LightningCreatorMismatchException("유저와 번개 생성자가 같지 않음");
    	}

    	// 유저가 신청대기 상태가 아닌 경우
    	if( !(joinUserEntity.getParticipantStatus() == ParticipantStatus.신청대기)) {
    		throw new LightningUserStatusNotPendingException("신청대기 상태가 아닌 경우");
    	}
    	
    	// 거절(탈퇴) 처리
        joinUserEntity.setParticipantStatus(LightningUserEntity.ParticipantStatus.탈퇴);
		
	}


    

}
