package com.taiso.bike_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taiso.bike_api.dto.LightningRequestDTO;
import com.taiso.bike_api.dto.LightningResponseDTO;
import com.taiso.bike_api.service.LightningService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@Slf4j
@RequestMapping("/api/lightnings")
@Tag(name = "번개 컨트롤러", description = "번개 관련 API")
public class LightningController {

    @Autowired
    LightningService lightningService;
    
    @PostMapping("")
    public ResponseEntity<LightningResponseDTO> createLighting(
        @RequestBody LightningRequestDTO requestDTO
        , @AuthenticationPrincipal String userEmail) {

        LightningResponseDTO responseDTO = lightningService.createLightning(requestDTO, userEmail);

        return ResponseEntity.noContent().build();
    }

    // 번개 리스트 조회
    // 정렬, 성별, 레벨, 자전거 타입, 지역, 태그 등을 기준으로 정렬 및 필터링하여 리턴
    // 해당 인풋값들은 필수는 아니며 기본정렬기준은 생성일순
    // GET /api/lightnings
    // @GetMapping("")
    // public ResponseEntity<List<GetLightningResponseDTO>> getLightnings(@RequestBody ) {

    //     return ResponseEntity.status(HttpStatus.SC_OK).body();
    // }
    
    
}
