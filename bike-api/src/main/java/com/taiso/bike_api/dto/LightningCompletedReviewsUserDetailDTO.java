package com.taiso.bike_api.dto;

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
public class LightningCompletedReviewsUserDetailDTO {
	
	// 닉네임&프사 (출력용) UserDetailEntity
	private Long userId;
    private String reviewedNickname;
    private String reviewedProfileImg;

}
