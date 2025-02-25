package com.taiso.bike_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taiso.bike_api.domain.UserEntity;
import com.taiso.bike_api.domain.UserReviewEntity;
import com.taiso.bike_api.dto.UserReviewRequestDTO;

public interface UserReviewRepository extends JpaRepository<UserReviewEntity, Long> {

	Optional<UserReviewRequestDTO> findByReviewTag(String reviewTag);

	Optional<UserReviewEntity> findByReviewerAndReviewed(UserEntity reviewer, UserEntity reviewedUser);

}
