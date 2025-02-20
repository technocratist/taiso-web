package com.taiso.bike_api.repository;

import com.taiso.bike_api.domain.LightningEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LightningDetailRepository extends JpaRepository<LightningEntity, Long> {

//    Optional<LightningEntity> existsByUser_UserIdAndLightning_CreatorId(Long userId, Long creatorId);

//    Boolean existsByUser_UserIdAndLightning_CreatorId(Long userId, Long creatorId);

}