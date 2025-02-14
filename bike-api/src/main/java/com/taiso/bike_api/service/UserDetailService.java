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
import software.amazon.awssdk.services.s3.S3Client;

import java.security.PrivateKey;
import java.util.Optional;

@Slf4j
@Service
public class UserDetailService {

    @Autowired
    S3Service s3Service;

    @Autowired
    private UserDetailRepository userDetailRepository;

    //이미지를 S3에 저장하기
    @Transactional
    public void updateUserDetail (UserDetailRequestDTO userDetailRequestDTO,MultipartFile profileImg, MultipartFile backgroundImg) {

        log.info(userDetailRequestDTO.toString());

        //이미지 파일 저장 + Id 발급
        String profileImgId = s3Service.uploadFile(profileImg,userDetailRequestDTO.getUserId());
        String backgroundImgId = s3Service.uploadFile(backgroundImg,userDetailRequestDTO.getUserId());

        //이미지 Id 삽입
        userDetailRequestDTO.setProfileImg(profileImgId);
        userDetailRequestDTO.setBackgroundImg(backgroundImgId);

        log.info(userDetailRequestDTO.toString());

        //Entity에 저장
        UserDetailEntity userDetailEntity = UserDetailEntity.builder().build();

        log.info(userDetailEntity.toString());

        //DB 저장
        userDetailRepository.save(userDetailEntity);

        //종료하기
        s3Service.close();
    }

    //S3에서 이미지를 불러오기
    @Transactional
    public UserDetailResponseDTO getUserDetail (Long userId) {

        //userDetailId로 해당 값 찾기
        Optional<UserDetailEntity> userDetail = userDetailRepository.findById(userId);

        //존재하면 파일 찾아오기
        UserDetailResponseDTO userDetailResponseDTO = null;
        if (userDetail.isPresent()) {
            byte[] profieImg = s3Service.getFile(userDetail.get().getUserProfileImg());
            byte[] backgroundImg = s3Service.getFile(userDetail.get().getUserBackgroundImg());

            //Entity -> DTO 로 builder
            userDetailResponseDTO = UserDetailResponseDTO.builder()
                    .userId(userDetail.get().getUserId())
                    .userNickname(userDetail.get().getUserNickname())
                    .bio(userDetail.get().getBio())
                    .profileImg(profieImg)
                    .backgroundImg(backgroundImg)
                    .build();
        }

        //종료하기
        s3Service.close();

        return userDetailResponseDTO;
    }

}
