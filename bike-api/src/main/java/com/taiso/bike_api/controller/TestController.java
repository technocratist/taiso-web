package com.taiso.bike_api.controller;

import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taiso.bike_api.dto.TestResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Tag(name = "TestController", description = "테스트 관련 API")
public class TestController {

    @GetMapping("/api/test")
    @Operation(summary = "인증 테스트", description = "인증 테스트 API")
    public TestResponseDTO test(Authentication authentication) {    
        log.info("TestController.test() 호출됨");
        // 인증 정보 출력
        System.out.println("authentication: " + authentication.getName());
        return new TestResponseDTO(authentication.getName(), LocalDateTime.now().toString(), "success");
    }
}
