package com.taiso.bike_api;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.taiso.bike_api.domain.UserEntity;
import com.taiso.bike_api.repository.UserRepository;
import com.taiso.bike_api.repository.UserRoleRepository;
import com.taiso.bike_api.repository.UserStatusRepository;
    

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

    @Override
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


        //테스트 아이디 추가
        UserEntity user = UserEntity.builder()
            .email("test@test.com")
            .password(passwordEncoder.encode("test"))
            .role(userRoleRepository.findByRoleName("USER").get())
            .status(userStatusRepository.findByStatusName("ACTIVE").get())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        userRepository.save(user);

        

    }
}
