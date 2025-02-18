package com.taiso.bike_api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.taiso.bike_api.domain.UserDetailEntity;
import com.taiso.bike_api.domain.UserEntity;
import com.taiso.bike_api.dto.RegisterRequestDTO;
import com.taiso.bike_api.dto.RegisterResponseDTO;
import com.taiso.bike_api.exception.EmailAlreadyExistsException;
import com.taiso.bike_api.exception.UserNotFoundException;
import com.taiso.bike_api.repository.UserDetailRepository;
import com.taiso.bike_api.repository.UserRepository;
import com.taiso.bike_api.repository.UserRoleRepository;
import com.taiso.bike_api.repository.UserStatusRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

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

    @Transactional
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
                .role(userRoleRepository.findByRoleName("USER")
                        .orElseThrow(() -> new RuntimeException("Role not found")))
                .status(userStatusRepository.findByStatusName("ACTIVE")
                        .orElseThrow(() -> new RuntimeException("Status not found")))
                .build();

        //레이스 컨디션 발생시, db unique 제약조건 위반 예외 처리
        try {
            UserEntity savedUser = userRepository.save(user);
            userRepository.flush();

            // 신규: 가입된 사용자에 대한 user detail 레코드를 생성하고 연결
            UserDetailEntity userDetail = UserDetailEntity.builder()
                    .user(savedUser)
                    // 필요에 따라 추가 필드 설정 (예: 기본값 등)
                    .build();
            userDetailRepository.save(userDetail);

            return new RegisterResponseDTO(savedUser.getUserId(), savedUser.getEmail());
        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyExistsException("이미 사용 중인 이메일입니다.");
        }

    }
    

    public Long getUserIdByEmail(String email) {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return user.get().getUserId();
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    
} 