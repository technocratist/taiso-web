package com.taiso.bike_api.dto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.taiso.bike_api.domain.LightningEntity.BikeType;
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
public class UserDetailPostRequestDTO {
    private String userNickname;
    private Gender gender;
    private LocalDate birthDate;
    private String phoneNumber;
    private String fullName;
    private String bio;
    private Set<ActivityTime> activityTime;
    private Set<ActivityDay> activityDay;    
    private Set<ActivityLocation> activityLocation;
    private Set<BikeType> bikeType;
    private Level level;
    private Integer FTP;
    private Integer height;
    private Integer weight;
    private Set<String> tags = new HashSet<>();

    public enum ActivityTime {
        오전, 오후, 저녁
    }

    public enum ActivityDay {
        월, 화, 수, 목, 금, 토, 일
    }

    public enum ActivityLocation {
        서울, 경기, 인천, 부산, 대구, 광주, 대전, 울산, 경상북도, 경상남도,
        전라남도, 전라북도, 충청남도, 충청북도, 강원도, 제주도,
    }
}
