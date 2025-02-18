package com.taiso.bike_api.dto;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoUserInfoDTO {
    private Long id;
    private Map<String, Object> properties;
    private KakaoAccount kakao_account;

    // 편의 메서드: 카카오에서 제공하는 닉네임 추출
    public String getNickname() {
        return properties != null ? (String) properties.get("nickname") : null;
    }

    public String getEmail() {
        return kakao_account != null ? kakao_account.getEmail() : null;
    }
}
