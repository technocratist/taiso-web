package com.taiso.bike_api.controller;

import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taiso.bike_api.dto.TestResponseDTO;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class TestController {

    @GetMapping("/api/test")
    public TestResponseDTO test(Authentication authentication) {
        log.info("TestController.test() 호출됨");
        // 인증 정보 출력
        System.out.println("authentication: " + authentication);
        return new TestResponseDTO(authentication.getName(), LocalDateTime.now().toString(), "success");
    }
}
