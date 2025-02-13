package com.taiso.bike_api.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailRequestDTO {

    private String userNickname;
    private String vio;

}
