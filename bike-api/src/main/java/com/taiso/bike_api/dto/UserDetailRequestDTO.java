package com.taiso.bike_api.dto;

import com.taiso.bike_api.service.S3Service;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailRequestDTO {

    private Long userId;
    private String userNickname;
    private String vio;
    private String profileImg;
    private String backgroundImg;


}
