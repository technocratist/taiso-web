package com.taiso.bike_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.taiso.bike_api.repository.MemberRepository;
import com.taiso.bike_api.security.CustomUserDetails;

// 사용자 세부 정보 서비스
@Service
public class CustomUserDetailService implements UserDetailsService {

    // 사용자 저장소 주입
    @Autowired
    private MemberRepository memberRepository;

    // 사용자 이름으로 사용자 세부 정보 로드
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 사용자 이름으로 사용자 찾은 후 사용자 세부 정보 반환
        return memberRepository.findByEmail(username).map(CustomUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }
}