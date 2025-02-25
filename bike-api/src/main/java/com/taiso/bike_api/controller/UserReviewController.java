package com.taiso.bike_api.controller;

import com.taiso.bike_api.dto.UserReviewResponseDTO;
import com.taiso.bike_api.service.UserDetailService;
import com.taiso.bike_api.service.UserReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/users")
@Tag(name = "회원 리뷰 컨트롤러", description = "회원정보 중 리뷰 관련 API")
public class UserReviewController {

    @Autowired
    private UserReviewService userReviewService;

    @Operation(summary = "회원 리뷰 리스트", description = "받은 리뷰 리스트 조회 API")
    @GetMapping("/{userId}/review")
    public ResponseEntity<List<UserReviewResponseDTO>> getReviewList (
            @PathVariable(name = "UserId") Long userId) {

        List<UserReviewResponseDTO> listUserReviewResponseDTO = userReviewService.getAllReview(userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(listUserReviewResponseDTO);
    }
}
