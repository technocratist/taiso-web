package com.taiso.bike_api.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

    // JWT 서명에 사용할 비밀키 (실제 운영 환경에서는 안전하게 보관)
    private final String JWT_SECRET = "jkajvdlavjsdjiasjvijdovajiojvisvasovnsvnadjfdsoiajfsodjo";

    // 토큰 유효시간 (예: 7일)
    private final long JWT_EXPIRATION = 1000L * 10; // 10초

    // 비밀키 객체 생성 (JWT_SECRET 문자열을 바이트 배열로 변환 후 키 객체 생성)
    private final Key key;

    public JwtTokenProvider() {
        this.key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
    }

    // JWT 토큰 생성 (jjwt 0.12.0 이상 방식 사용)
    public String generateToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
        
        return Jwts.builder()
                .header()                                   // (2) optional
                .keyId("aKeyId")
                .and()
                .expiration(expiryDate)
                .subject(email)                             // (3) JSON Claims, or
                .signWith(key)                       // (4) if signing, or
                .compact();                                 // (5)
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key) // use byte[] to avoid deprecation
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    // JWT 토큰 검증
    public boolean validateToken(String token) {
        try {
            // JWT 파서 빌더로 토큰 파싱 및 서명 검증 시도
            Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            // 토큰 서명 검증 실패
            System.out.println("Invalid JWT signature.");
        } catch (ExpiredJwtException e) {
            // 토큰 만료
            System.out.println("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            // 지원하지 않는 토큰
            System.out.println("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            // 토큰 클레임이 비어있음
            System.out.println("JWT claims string is empty.");
        }
        return false;
    }
}