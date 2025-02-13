package com.taiso.bike_api.controller;

import com.taiso.bike_api.dto.UserDetailRequestDTO;
import com.taiso.bike_api.dto.UserDetailResponseDTO;
import com.taiso.bike_api.service.UserDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequestMapping("/api/users")
@Tag(name = "회원 컨트롤러", description = "회원 정보 관련 API")
public class UserController {

    @Autowired
    private UserDetailService userDetailService;

    @PostMapping("/me/details")
    @Operation(summary = "내 페이지 정보 수정", description = "상세 프로필 정보 수정")
    public ResponseEntity<UserDetailResponseDTO> updateUserDetail(@RequestBody UserDetailRequestDTO userDetailRequestDTO
                                                                , @RequestPart("profileImg") MultipartFile profileImg
                                                                , @RequestPart("backgroundImg") MultipartFile backfroundImg
                                                                , HttpServletResponse httpServletResponse){
        log.info(userDetailRequestDTO.toString());
        UserDetailResponseDTO userDetailResponseDTO = userDetailService.updateUserDetail(userDetailRequestDTO);

        //return ResponseEntity.status(201).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(userDetailResponseDTO);
    }

}
