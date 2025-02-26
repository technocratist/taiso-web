package com.taiso.bike_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taiso.bike_api.domain.LightningEntity;
import com.taiso.bike_api.domain.UserDetailEntity;
import com.taiso.bike_api.domain.UserEntity;
import com.taiso.bike_api.domain.UserReviewEntity;
import com.taiso.bike_api.dto.UserReviewRequestDTO;

public interface UserReviewRepository extends JpaRepository<UserReviewEntity, Long> {

	Optional<UserReviewRequestDTO> findByReviewTag(String reviewTag);

	List<UserReviewEntity> findByReviewer(UserEntity userEntity);
	
	List<UserReviewEntity> findByReviewer_UserId(Long userId);

	// 리뷰 삭제 repository
	Optional<UserReviewEntity> findByReviewerAndReviewed(UserDetailEntity reviewer, UserDetailEntity reviewedUser);



}
