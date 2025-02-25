package com.taiso.bike_api.repository;

import com.taiso.bike_api.domain.ClubBoardEntity;
import com.taiso.bike_api.domain.UserReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserReviewRepository extends JpaRepository<UserReviewEntity, Long> {

    List<UserReviewEntity> findByReviewed_UserId(Long userId);
}
