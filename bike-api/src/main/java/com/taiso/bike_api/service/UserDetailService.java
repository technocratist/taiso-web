package com.taiso.bike_api.service;

import com.taiso.bike_api.domain.UserDetailEntity;
import com.taiso.bike_api.dto.UserDetailRequestDTO;
import com.taiso.bike_api.dto.UserDetailResponseDTO;
import com.taiso.bike_api.repository.UserDetailRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.security.PrivateKey;

@Slf4j
@Service
public class UserDetailService {

    @Autowired
    S3Service s3Service;

    @Autowired
    private UserDetailRepository userDetailRepository;

    @Transactional
    public void updateUserDetail(UserDetailRequestDTO userDetailRequestDTO,MultipartFile profileImg, MultipartFile backgroundImg) {

        log.info(userDetailRequestDTO.toString());

        // 이미지 파일 저장 + Id 발급
        String profileImgId = s3Service.uploadFile(profileImg,userDetailRequestDTO.getUserId());
        String backgroundImgId = s3Service.uploadFile(backgroundImg,userDetailRequestDTO.getUserId());

        // 이미지 Id 삽입
        userDetailRequestDTO.setProfileImg(profileImgId);
        userDetailRequestDTO.setBackgroundImg(backgroundImgId);

        log.info(userDetailRequestDTO.toString());

        //Entity에 저장
        UserDetailEntity userDetailEntity = UserDetailEntity.builder().build();

        log.info(userDetailEntity.toString());

        //DB 저장
        userDetailRepository.save(userDetailEntity);
    }




}
