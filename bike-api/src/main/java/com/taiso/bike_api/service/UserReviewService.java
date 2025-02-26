package com.taiso.bike_api.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.taiso.bike_api.domain.UserDetailEntity;
import com.taiso.bike_api.domain.UserEntity;
import com.taiso.bike_api.domain.UserReviewEntity;
import com.taiso.bike_api.dto.UserLightningReviewResponseDTO;
import com.taiso.bike_api.exception.UserNotFoundException;
import com.taiso.bike_api.repository.UserDetailRepository;
import com.taiso.bike_api.repository.UserRepository;
import com.taiso.bike_api.repository.UserReviewRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserReviewService {


	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserReviewRepository userReviewRepository;
	
	// 리뷰 목록 출력 
	public List<UserLightningReviewResponseDTO> myLightningReviews(Authentication authentication) {
		
    	// 1. 현재 로그인한 사용자 (리뷰 작성자) 조회
        UserEntity userEntity = userRepository.findByEmail(authentication.getName())
        		// 사용자 찾을 수 없음 -> 404
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다.")); 
        
        // 리뷰 작성자 기준 리뷰 조회
        List<UserReviewEntity> reviewEntities = userReviewRepository.findByReviewer_UserId(userEntity.getUserId());
        
        // 엔티티를 DTO로 매핑
        List<UserLightningReviewResponseDTO> responseDTOs = reviewEntities.stream()
            .map((UserReviewEntity review) -> UserLightningReviewResponseDTO.builder()
//            		.map(review -> UserLightningReviewResponseDTO.builder()
                .reviewId(review.getReviewId())
                .reviewer(review.getReviewer().getUserId())
                .reviewed(review.getReviewed().getUserId())
                .lightningId(review.getLightning().getLightningId())
                
                // 내용, 태그, 시간
                .reviewContent(review.getReviewContent())
                .reviewTag(review.getReviewTag())
                .createdAt(review.getCreatedAt())
                
                // 닉네임
                .reviewerNickname(review.getReviewer().getUserNickname())
                .reviewedNickname(review.getReviewed().getUserNickname())
                .reviewerProfileImg(review.getReviewer().getUserProfileImg())
                .reviewedProfileImg(review.getReviewed().getUserProfileImg())
                
                .build()
            )
            .collect(Collectors.toList());
        
        log.info("reviewEntities {}",responseDTOs);
        return responseDTOs;
        
	}
	
	
	
}
