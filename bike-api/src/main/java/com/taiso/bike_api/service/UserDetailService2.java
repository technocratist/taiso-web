package com.taiso.bike_api.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taiso.bike_api.domain.UserDetailEntity;
import com.taiso.bike_api.domain.UserEntity;
import com.taiso.bike_api.domain.UserTagCategoryEntity;
import com.taiso.bike_api.dto.UserDetailGetResponseDTO;
import com.taiso.bike_api.dto.UserDetailPatchRequestDTO;
import com.taiso.bike_api.dto.UserDetailPostRequestDTO;
import com.taiso.bike_api.exception.TagsNotFoundException;
import com.taiso.bike_api.exception.UserDetailNotFoundException;
import com.taiso.bike_api.exception.UserNotFoundException;
import com.taiso.bike_api.repository.UserDetailRepository;
import com.taiso.bike_api.repository.UserRepository;
import com.taiso.bike_api.repository.UserTagCategoryRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserDetailService2 {

    @Autowired
    private UserDetailRepository userDetailRepository;

    @Autowired
    private UserTagCategoryRepository userTagCategoryRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public void saveUserDetail(UserDetailPostRequestDTO requestDTO, String userEmail) {

        // 사용자 정보 가져오기
        UserEntity user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다."));
        
        // tag 종합하기
        requestDTO.getTags().addAll(requestDTO.getBikeType().stream().map(bikeType -> bikeType.name()).collect(Collectors.toSet()));
        requestDTO.getTags().addAll(requestDTO.getActivityDay().stream().map(activityDay -> activityDay.name()).collect(Collectors.toSet()));
        requestDTO.getTags().addAll(requestDTO.getActivityTime().stream().map(activityTime -> activityTime.name()).collect(Collectors.toSet()));
        requestDTO.getTags().addAll(requestDTO.getActivityLocation().stream().map(activityLocation -> activityLocation.name()).collect(Collectors.toSet()));

        log.info("{}", requestDTO.getTags());

        // tag 엔티티 가져오기
        Set<UserTagCategoryEntity> tags = requestDTO.getTags().stream().map(tag -> userTagCategoryRepository.findByName(tag)
        .orElseThrow(() -> new TagsNotFoundException("태그 정보가 없습니다.")))
        .collect(Collectors.toSet());

        log.info("{}", requestDTO.getFTP());

        // UserDetailEntity 생성
        UserDetailEntity userDetail = UserDetailEntity.builder()
                .userId(user.getUserId())
                .user(user)
                .gender(requestDTO.getGender())
                .birthDate(requestDTO.getBirthDate())
                .userNickname(requestDTO.getUserNickname())
                .fullName(requestDTO.getFullName())
                .phoneNumber(requestDTO.getPhoneNumber())
                .bio(requestDTO.getBio())
                .level(requestDTO.getLevel())
                .FTP(requestDTO.getFTP())
                .height(requestDTO.getHeight())
                .weight(requestDTO.getWeight())
                .tags(tags)
                .build();

        // UserDetailEntity 저장
        userDetailRepository.save(userDetail);
    }

    public UserDetailGetResponseDTO getUserDetail(String userEmail) {
        // 사용자 정보 가져오기
        UserEntity user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다."));
        
        // UserDetailEntity 가져오기
        UserDetailEntity userDetail = userDetailRepository.findByUserId(user.getUserId()).orElseThrow(() -> new UserDetailNotFoundException("사용자 상세정보가 없습니다."));

        // UserDetailGetResponseDTO 생성
        return UserDetailGetResponseDTO.builder()
                .userId(userDetail.getUserId())
                .gender(userDetail.getGender())
                .birthDate(userDetail.getBirthDate())
                .userNickname(userDetail.getUserNickname())
                .fullName(userDetail.getFullName())
                .phoneNumber(userDetail.getPhoneNumber())
                .bio(userDetail.getBio())
                .level(userDetail.getLevel())
                .gender(userDetail.getGender())
                .FTP(userDetail.getFTP())
                .height(userDetail.getHeight())
                .weight(userDetail.getWeight())
                .tags(userDetail.getTags().stream().map(tag -> tag.getName()).collect(Collectors.toSet()))
                .build();
    }

    @Transactional
    public void patchUserDetail(String userEmail, UserDetailPatchRequestDTO requestDTO) {
        // 사용자의 상세정보 가져오기
        UserDetailEntity userDetail = userDetailRepository.findByUserId(userRepository.findByEmail(userEmail).get().getUserId()).get();

        log.info("{}", requestDTO);

        // 수정하기
        if(requestDTO.getNickname() != null) {
            userDetail.setUserNickname(requestDTO.getNickname());
        }
        if(requestDTO.getUserProfileImg() != null) {
            userDetail.setUserProfileImg(requestDTO.getUserProfileImg());
        }
        if(requestDTO.getUserBackgroundImg() != null) {
            userDetail.setUserBackgroundImg(requestDTO.getUserBackgroundImg());
        }
        if(requestDTO.getPhoneNumber() != null) {
            userDetail.setPhoneNumber(requestDTO.getPhoneNumber());
        }
        if(requestDTO.getBirthDate() != null) {
            userDetail.setBirthDate(requestDTO.getBirthDate());
        }
        if(requestDTO.getBio() != null) {
            userDetail.setBio(requestDTO.getBio());
        }
        if(requestDTO.getGender() != null) {
            userDetail.setGender(requestDTO.getGender());
        }
        if(requestDTO.getLevel() != null) {
            userDetail.setLevel(requestDTO.getLevel());
        }
        if(requestDTO.getHeight() != null) {
            userDetail.setHeight(requestDTO.getHeight());
        }
        if(requestDTO.getWeight() != null) {
            userDetail.setWeight(requestDTO.getWeight());
        }
        if(requestDTO.getFTP() != null) {
            userDetail.setFTP(requestDTO.getFTP());
        }

        log.info("{}", userDetail);

        userDetailRepository.save(userDetail);

    }
}
