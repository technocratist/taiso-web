package com.taiso.bike_api.dto;

import java.time.LocalDate;
import java.util.Set;

import com.taiso.bike_api.domain.UserDetailEntity.Gender;
import com.taiso.bike_api.domain.UserDetailEntity.Level;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class UserDetailGetResponseDTO {
    private Long userId;
    private String userNickname;
    private String userProfileImg;
    private String userBackgroundImg;
    private String fullName;
    private String phoneNumber;
    private LocalDate birthDate;
    private String bio;
    private Gender gender;
    private Level level;
    private Integer FTP;
    private Integer height;
    private Integer weight;
    private Set<String> tags;
}
