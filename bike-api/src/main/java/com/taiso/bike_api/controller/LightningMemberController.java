package com.taiso.bike_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taiso.bike_api.dto.JoinParticipantsPostResponseDTO;
import com.taiso.bike_api.service.LightningMemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/lightnings")
@Tag(name = "번개 멤버 컨트롤러", description = "번개 멤버 관련 API")
public class LightningMemberController {

    @Autowired
    private LightningMemberService lightningMemberService;

	@PostMapping("{lightningId}/participants")
  @Operation(summary = "번개 참가 및 참가 신청", description = "번개에 현재 사용자 참가 및 참가 신청 API")
	public ResponseEntity<JoinParticipantsPostResponseDTO> join(
    		@PathVariable(name = "lightningId") Long lightningId,
			Authentication authentication
			) {
		
		lightningMemberService.JoinParticipants(lightningId, authentication);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(null);
	}
	
	// 인원 다 찼을 때 마감 Service.autoClose 도착했을 때의 TEST
//  @Operation(summary = "번개 마감", description = "인원 가득 찼을 때 마감 수정 테스트") 
//	@PatchMapping("{lightningId}/auto-close")
//	public void autoClose(
//    		@PathVariable(name = "lightningId") Long lightningId
//			){
//		lightningMemberService.autoClose(lightningId);
//	}
	
    
    // 번개 강제 마감
    @Operation(summary = "번개 강제 마감", description = "번개 강제 마감 API")
    @PatchMapping("{lightningId}/close")  
    public ResponseEntity<JoinParticipantsPostResponseDTO> lightningClose(
    		@PathVariable(name = "lightningId") Long lightningId,
			Authentication authentication
    		) {
    	
    	lightningMemberService.lightningClose(lightningId, authentication);

		return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
    

    
}
