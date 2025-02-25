package com.taiso.bike_api.dto;

import com.taiso.bike_api.domain.UserReviewEntity.ReviewTag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserReviewRequestDTO {
    private String reviewContent;
    private ReviewTag reviewTag; // EXCELLENT, GOOD, AVERAGE, POOR 중 하나
}
