package com.taiso.bike_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taiso.bike_api.domain.LightningEntity;

public interface LightningRepository extends JpaRepository<LightningEntity, Long> {

}
