package com.taiso.bike_api.service;

import com.taiso.bike_api.domain.UserReviewEntity;
import com.taiso.bike_api.dto.UserReviewResponseDTO;
import com.taiso.bike_api.exception.LightningNotFoundException;
import com.taiso.bike_api.exception.NotPermissionException;
import com.taiso.bike_api.exception.UserReviewNotFoundException;
import com.taiso.bike_api.repository.UserDetailRepository;
import com.taiso.bike_api.repository.UserReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserReviewService {

    @Autowired
    private UserReviewRepository userReviewRepository;

    public List<UserReviewResponseDTO> getAllReview(Long userId) {

        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException(userId + " 값은 올바르지 않음");
        }

        List<UserReviewEntity> allReview = userReviewRepository.findByReviewed_UserId(userId);

        if (allReview.isEmpty()) {
            throw new UserReviewNotFoundException("받은 리뷰가 존재하지 않습니다.");
        }

        log.info("유저 아이디로 찾아온 리뷰들 : {}", allReview.stream().toList());

        return allReview.stream()
                .map(entity -> UserReviewResponseDTO.builder()
                        .reviewId(entity.getReviewId())
                        .reviewed(entity.getReviewed().getUserId())
                        .reviewer(entity.getReviewer().getUserId())
                        // 각 유저 닉네임
                        .reviewedNickname(entity.getReviewed().getUserNickname())
                        .reviewerNickname(entity.getReviewer().getUserNickname())
                        // 리뷰 작성 유저의 프로필 사진
                        .reviewerProfileImg(entity.getReviewer().getUserProfileImg())
                        .reviewContent(entity.getReviewContent())
                        .reviewTag(entity.getReviewTag().name()) // Enum → String 변환
                        .createdAt(entity.getCreatedAt())
                        .updatedAt(entity.getUpdatedAt())
                        .lightningId(entity.getLightning().getLightningId())
                        .build()
                )
                .collect(Collectors.toList()); // DTO 리스트로 변환
    }

    }