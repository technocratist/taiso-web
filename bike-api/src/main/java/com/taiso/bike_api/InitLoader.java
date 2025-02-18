package com.taiso.bike_api;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.taiso.bike_api.domain.UserDetailEntity;
import com.taiso.bike_api.domain.UserEntity;
import com.taiso.bike_api.repository.UserDetailRepository;
import com.taiso.bike_api.repository.UserRepository;
import com.taiso.bike_api.repository.UserRoleRepository;
import com.taiso.bike_api.repository.UserStatusRepository;

import jakarta.transaction.Transactional;
    

@Component
public class InitLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserStatusRepository userStatusRepository;

    @Autowired
    private UserDetailRepository userDetailRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // // role 추가
        // UserRoleEntity role = UserRoleEntity.builder()
        //     .roleName("USER")
        //     .createdAt(LocalDateTime.now())
        //     .updatedAt(LocalDateTime.now())
        //     .build();
        // userRoleRepository.save(role);

        // // status 추가
        // UserStatusEntity status = UserStatusEntity.builder()
        //     .statusName("ACTIVE")
        //     .createdAt(LocalDateTime.now())
        //     .updatedAt(LocalDateTime.now())
        //     .build();
        // userStatusRepository.save(status);


        // 테스트 아이디 추가
        UserEntity user = UserEntity.builder()
                .email("test@test.com")
                .password(passwordEncoder.encode("test"))
                .role(userRoleRepository.findByRoleName("USER").get())
                .status(userStatusRepository.findByStatusName("ACTIVE").get())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // 사용자 저장 후 ID가 할당됨
        userRepository.saveAndFlush(user);

        // UserDetailEntity 저장
        UserDetailEntity userDetail = UserDetailEntity.builder()
                .userNickname("무면허라이더")
                .bio("처음뵙겠습니다.")
                .FTP(134)
                .gender(UserDetailEntity.Gender.valueOf("여자"))
                .level(UserDetailEntity.Level.valueOf("초보자"))
                .birthDate(LocalDateTime.now())
                .fullName("권혜연")
                .phoneNumber("010-5529-7835")
                .height(158)
                .weight(48)
                .FTP(120)
                .user(user)  // user 객체를 연결
                .build();

        // userDetail 저장
        userDetailRepository.save(userDetail);
    }
}
