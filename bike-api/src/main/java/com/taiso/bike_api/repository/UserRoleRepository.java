package com.taiso.bike_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taiso.bike_api.domain.UserRoleEntity;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Integer> {

}
