package com.taiso.bike_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taiso.bike_api.domain.ClubEntity;

@Repository
public interface ClubRepository extends JpaRepository<ClubEntity, Long> {}
