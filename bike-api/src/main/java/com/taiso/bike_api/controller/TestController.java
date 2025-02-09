package com.taiso.bike_api.controller;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taiso.bike_api.dto.TestResponseDTO;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class TestController {

    @GetMapping("/api/test")
    public TestResponseDTO test() {
        log.info("TestController.test() 호출됨");
        return new TestResponseDTO("Hello, World!", LocalDateTime.now().toString(), "success");
    }
}
