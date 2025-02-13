package com.taiso.bike_api.service;

import com.taiso.bike_api.domain.UserDetailEntity;
import com.taiso.bike_api.dto.UserDetailRequestDTO;
import com.taiso.bike_api.dto.UserDetailResponseDTO;
import com.taiso.bike_api.repository.UserDetailRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserDetailService {

    @Autowired
    private UserDetailRepository userDetailRepository;

    @Transactional
    public UserDetailResponseDTO updateUserDetail(UserDetailRequestDTO userDetailRequestDTO) {

        UserDetailEntity userDetailEntity = new UserDetailEntity();

        userDetailEntity.setUserNickname(userDetailRequestDTO.getUserNickname());
        userDetailEntity.setBio(userDetailRequestDTO.getVio());

        log.info(userDetailEntity.toString());

        UserDetailResponseDTO userDetailResponseDTO = new UserDetailResponseDTO();
        userDetailResponseDTO.setUserNickname(userDetailEntity.getUserNickname());
        userDetailResponseDTO.setVio(userDetailEntity.getBio());

        log.info(userDetailResponseDTO.toString());

        return userDetailResponseDTO;
    }

}
