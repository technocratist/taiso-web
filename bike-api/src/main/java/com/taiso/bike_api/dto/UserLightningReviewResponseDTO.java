package com.taiso.bike_api.dto;

import java.time.LocalDateTime;

import com.taiso.bike_api.domain.LightningEntity;
import com.taiso.bike_api.domain.UserEntity;
import com.taiso.bike_api.domain.UserReviewEntity.ReviewTag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserLightningReviewResponseDTO {

    private Long reviewId;
    
	private UserEntity reviewer;

    private UserEntity reviewed;

    private String reviewContent;

	private LightningEntity lightning;

    private ReviewTag reviewTag;

    private LocalDateTime createdAt;
	
	
}
