package com.taiso.bike_api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.taiso.bike_api.domain.UserEntity;
import com.taiso.bike_api.dto.RegisterRequestDTO;
import com.taiso.bike_api.dto.RegisterResponseDTO;
import com.taiso.bike_api.exception.EmailAlreadyExistsException;
import com.taiso.bike_api.repository.UserRepository;

@Service
public class MemberService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public RegisterResponseDTO register(RegisterRequestDTO dto) {
        // 이메일 중복 체크
        Optional<UserEntity> existingMember = userRepository.findByEmail(dto.getEmail());
        if (existingMember.isPresent()) {
            throw new EmailAlreadyExistsException("이미 사용 중인 이메일입니다.");
        }

        // MemberEntity 생성 (roleId=1, statusId=1 은 기본값으로 설정)
        UserEntity user = UserEntity.builder()
            .email(dto.getEmail())
            .password(passwordEncoder.encode(dto.getPassword()))
            .roleId(1)     // 기본 사용자 역할
            .statusId(1)   // 활성 상태
            .build();

        UserEntity savedUser = userRepository.save(user);

        return new RegisterResponseDTO(savedUser.getUserId(), savedUser.getEmail(), "회원가입 성공");
    }
} 