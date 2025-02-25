package com.taiso.bike_api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.taiso.bike_api.domain.UserEntity;
import com.taiso.bike_api.domain.UserReviewEntity;
import com.taiso.bike_api.dto.UserLightningReviewResponseDTO;
import com.taiso.bike_api.exception.UserNotFoundException;
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
    @Transactional
//	public UserLightningReviewResponseDTO myLightningReviews(Authentication authentication) {
    	public List<UserLightningReviewResponseDTO> myLightningReviews(Authentication authentication) {
		
    	// 1. 현재 로그인한 사용자 (리뷰 작성자) 조회
        UserEntity userEntity = userRepository.findByEmail(authentication.getName())
        		// 사용자 찾을 수 없음 -> 404
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다.")); 
        
        // 리뷰 작성자 기준 리뷰 조회
        List<UserReviewEntity> reviewEntities = userReviewRepository.findByReviewer(userEntity);

        // 엔티티를 DTO로 매핑
        List<UserLightningReviewResponseDTO> responseDTOs = reviewEntities.stream()
            .map(review -> UserLightningReviewResponseDTO.builder()
                .reviewId(review.getReviewId())
                .reviewer(review.getReviewer())
                .reviewed(review.getReviewed())
                .reviewContent(review.getReviewContent())
                .lightning(review.getLightning())
                .reviewTag(review.getReviewTag())
                .createdAt(review.getCreatedAt())
                .build()
            )
            .collect(Collectors.toList());
        
        log.info("reviewEntities {}",responseDTOs);
        return responseDTOs;
        
	}
	
	
	
}
