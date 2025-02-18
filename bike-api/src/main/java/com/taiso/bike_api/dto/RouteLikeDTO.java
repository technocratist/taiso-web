package com.taiso.bike_api.dto;

import java.time.LocalDateTime;

import com.taiso.bike_api.domain.RouteEntity;
import com.taiso.bike_api.domain.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Builder
public class RouteLikeDTO {
	private Long routeLikeId;
	private RouteEntity route;
	private UserEntity user;
	private LocalDateTime likeAt;
}
