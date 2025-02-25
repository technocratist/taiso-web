package com.taiso.bike_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taiso.bike_api.dto.LightningsReviewsPostResponseDTO;
import com.taiso.bike_api.dto.UserReviewRequestDTO;
import com.taiso.bike_api.service.ReviewService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/lightnings")
@Tag(name = "리뷰 컨트롤러", description = "리뷰 관련 API")
public class ReviewController {
	
	@Autowired
    private ReviewService reviewService;
	
	@PostMapping("{lightningId}/reviews")
	public ResponseEntity<LightningsReviewsPostResponseDTO> LightningReviews(
			@PathVariable(name = "lightningId") Long lightningId,
			@RequestBody UserReviewRequestDTO userReviewRequestDTO,
			Authentication authentication,
			@RequestParam(value = "userId", required = false) Long userId
			) {
		
		reviewService.createReview(lightningId, userId, authentication, userReviewRequestDTO);

		return ResponseEntity.status(HttpStatus.CREATED).body(null);
	}
	
	@DeleteMapping("{lightningId}/reviews")
	public ResponseEntity<LightningsReviewsPostResponseDTO> lightningReviewsDelete(
			@PathVariable(name = "lightningId") Long lightningId,
			Authentication authentication,
			@RequestParam(value = "userId", required = false) Long userId
			) {

		reviewService.deleteReview(lightningId, userId, authentication);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
	}
	
	
	
	
	
}
