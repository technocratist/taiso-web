package com.taiso.bike_api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.taiso.bike_api.domain.MemberEntity;
import com.taiso.bike_api.dto.RegisterRequestDTO;
import com.taiso.bike_api.dto.RegisterResponseDTO;
import com.taiso.bike_api.exception.EmailAlreadyExistsException;
import com.taiso.bike_api.repository.MemberRepository;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public RegisterResponseDTO register(RegisterRequestDTO dto) {
        // 이메일 중복 체크
        Optional<MemberEntity> existingMember = memberRepository.findByEmail(dto.getEmail());
        if (existingMember.isPresent()) {
            throw new EmailAlreadyExistsException("이미 사용 중인 이메일입니다.");
        }

        // MemberEntity 생성 (roleId=1, statusId=1 은 기본값으로 설정)
        MemberEntity member = MemberEntity.builder()
            .email(dto.getEmail())
            .password(passwordEncoder.encode(dto.getPassword()))
            .roleId(1)     // 기본 사용자 역할
            .statusId(1)   // 활성 상태
            .build();

        MemberEntity savedMember = memberRepository.save(member);

        return new RegisterResponseDTO(savedMember.getUserId(), savedMember.getEmail(), "회원가입 성공");
    }
} 