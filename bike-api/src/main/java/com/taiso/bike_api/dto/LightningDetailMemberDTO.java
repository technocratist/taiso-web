package com.taiso.bike_api.dto;

import com.taiso.bike_api.domain.LightningUserEntity;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LightningDetailMemberDTO {

    private Long lightningUserId;
    private String participantStatus;
    private String role;

    private String memberNickname;
    private String memberProfileImg;



}
