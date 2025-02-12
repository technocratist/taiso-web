package com.taiso.bike_api;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.taiso.bike_api.domain.UserEntity;
import com.taiso.bike_api.repository.UserRepository;
    

@Component
public class InitLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        //테스트 아이디 추가
        UserEntity user = UserEntity.builder()
            .email("test@test.com")
            .password(passwordEncoder.encode("test"))
            .roleId(1)
            .statusId(1)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        userRepository.save(user);
    }
}
