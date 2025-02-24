package com.taiso.bike_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.taiso.bike_api.dto.LightingParticipationCheckResponseDTO;

import com.taiso.bike_api.dto.LightningGetRequestDTO;
import com.taiso.bike_api.dto.LightningGetResponseDTO;
import com.taiso.bike_api.dto.LightningRequestDTO;
import com.taiso.bike_api.dto.LightningResponseDTO;
import com.taiso.bike_api.service.LightningService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/lightnings")
@Tag(name = "번개 컨트롤러", description = "번개 관련 API")
public class LightningController {

    @Autowired
    LightningService lightningService;
    
    @PostMapping("")
    @Operation(summary = "번개 생성", description = "번개 생성 API")
    public ResponseEntity<LightningResponseDTO> createLighting(
        @RequestBody LightningRequestDTO requestDTO
        , @AuthenticationPrincipal String userEmail) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(lightningService.createLightning(requestDTO, userEmail));
    }

    @GetMapping("")
    public ResponseEntity<LightningGetResponseDTO> getLightning(@RequestBody LightningGetRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(lightningService.getLightning(requestDTO));
    }

    @GetMapping("/{lightningId}/participation")
    public ResponseEntity<LightingParticipationCheckResponseDTO> getLightningParticipationCheck(
    @PathVariable(name = "lightningId") Long lightningId
    , @AuthenticationPrincipal String userEmail) {
        return ResponseEntity.status(HttpStatus.OK).body(lightningService.getParticipationCheck(lightningId, userEmail));
    }
}
