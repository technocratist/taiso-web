package com.taiso.bike_api.service;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.taiso.bike_api.config.KakaoProperties;
import com.taiso.bike_api.domain.UserEntity;
import com.taiso.bike_api.dto.KakaoUserInfoDTO;
import com.taiso.bike_api.exception.KakaoAuthenticationException;
import com.taiso.bike_api.repository.UserRepository;
import com.taiso.bike_api.repository.UserRoleRepository;
import com.taiso.bike_api.repository.UserStatusRepository;
import com.taiso.bike_api.security.JwtTokenProvider;

@Service
public class AuthService {

    private final KakaoProperties kakaoProperties;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RestTemplate restTemplate;
    private final UserRoleRepository userRoleRepository;
    private final UserStatusRepository userStatusRepository;

    public AuthService(KakaoProperties kakaoProperties,
                       UserRepository userRepository,
                       JwtTokenProvider jwtTokenProvider,
                       UserRoleRepository userRoleRepository,
                       UserStatusRepository userStatusRepository) {
        this.kakaoProperties = kakaoProperties;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.restTemplate = new RestTemplate();
        this.userRoleRepository = userRoleRepository;
        this.userStatusRepository = userStatusRepository;
    }

    /**
     * 프론트엔드로부터 전달받은 인가 코드를 이용해
     * 카카오와 통신하고 JWT를 생성하여 반환함.
     */
    public String processKakaoLogin(String code) {
        // 1. 인가 코드로 액세스 토큰 교환
        String accessToken = getKakaoAccessToken(code);
        System.out.println("accessToken: " + accessToken);
        // 2. 액세스 토큰으로 사용자 정보 조회
        KakaoUserInfoDTO kakaoUserInfo = getKakaoUserInfo(accessToken);
        System.out.println("kakaoUserInfo: " + kakaoUserInfo);

        // 3. DB에 사용자 존재 여부 확인, 신규 가입 처리
UserEntity user = userRepository.findByEmail(kakaoUserInfo.getEmail())
        .orElseGet(() -> {
            UserEntity newUser = new UserEntity();
            newUser.setEmail(kakaoUserInfo.getEmail());
            // Set the default role and status (you need to replace these with actual default values)
            newUser.setRole(userRoleRepository.findByRoleName("USER").get());    // defaultRole: instance of UserRoleEntity
            newUser.setStatus(userStatusRepository.findByStatusName("ACTIVE").get()); // defaultStatus: instance of UserStatusEntity
            return userRepository.save(newUser);
        });

        
        // 4. JWT 생성 (여기서는 사용자 id와 역할 정보를 포함)
        return jwtTokenProvider.generateToken(user.getEmail());
    }

    private String getKakaoAccessToken(String code) {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoProperties.getClientId());
        params.add("redirect_uri", kakaoProperties.getRedirectUri());
        params.add("code", code);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);

        Map<String, Object> body = response.getBody();
        if (response.getStatusCode() == HttpStatus.OK && body != null) {
            String accessToken = (String) body.get("access_token");
            if (accessToken != null) {
                return accessToken;
            }
        }
        throw new KakaoAuthenticationException("카카오로부터 액세스 토큰을 받지 못했습니다.");
    }

    private KakaoUserInfoDTO getKakaoUserInfo(String accessToken) {
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserInfoDTO> response = restTemplate.exchange(
                userInfoUrl,
                HttpMethod.GET,
                entity,
                KakaoUserInfoDTO.class
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new KakaoAuthenticationException("카카오로부터 사용자 정보를 받지 못했습니다.");
        }
    }
}