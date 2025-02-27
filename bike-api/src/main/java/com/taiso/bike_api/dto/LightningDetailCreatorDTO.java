package com.taiso.bike_api.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LightningDetailCreatorDTO {

    private Long userId;
    private String creatorNickname;
    private String creatorProfileImg;
}
