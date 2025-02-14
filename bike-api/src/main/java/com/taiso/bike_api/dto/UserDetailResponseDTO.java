package com.taiso.bike_api.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailResponseDTO {

    private Long userId;
    private String userNickname;
    private String vio;
    private String profileImg;
    private String backgroundImg;

    public UserDetailResponseDTO(UserDetail user, FileService fileService) {
        this.userId = user.getId();
        this.nickname = user.getNickname();
        this.profileImgUrl = fileService.getFileUrl(user.getUserProfileImg());
        this.backgroundImgUrl = fileService.getFileUrl(user.getUserBackgroundImg());
    }

}
