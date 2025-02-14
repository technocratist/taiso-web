package com.taiso.bike_api.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailResponseDTO {

    private Long userId;
    private String userNickname;
    private String bio;
    private byte[] profileImg;
    private byte[] backgroundImg;

}
