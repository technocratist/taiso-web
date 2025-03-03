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
public class LightningDetailMemberDTO {

    private Long lightningUserId;
    private String participantStatus;
    private String role;

    private String memberNickname;
    private String memberProfileImg;

}
