package com.taiso.bike_api.dto;

import com.taiso.bike_api.domain.LightningUserEntity.ParticipantStatus;
import com.taiso.bike_api.domain.LightningUserEntity.Role;

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
public class LightningCompletedReviewsLightningUserDTO {

    // 번개 아이디
	private Long lightning;
    // 번개 회원 역할(생성자, 참여자) lightningUserEntity
    private Role role;
    // 번개 회원 상태 (신청대기, 승인, 탈퇴, 완료)
    private ParticipantStatus participantStatus;
}
