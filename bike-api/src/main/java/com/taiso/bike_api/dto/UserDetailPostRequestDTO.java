package com.taiso.bike_api.dto;

import java.time.LocalDateTime;

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
    private LocalDateTime birthDate;
    private String phoneNumber;
    private String fullName;
    private ActivityTime activityTime;
    private ActivityDay activityDay;
    private ActivityLocation activityLocation;
    private BikeType bikeType;
    private Level level;
    private Integer FTP;
    private Integer height;
    private Integer weight;

    public enum ActivityTime {
        MORNING, AFTERNOON, EVENING
    }

    public enum ActivityDay {
        MON, TUE, WED, THU, FRI, SAT, SUN
    }

    public enum ActivityLocation {
        SEOUL, GYEONGGI, INCHEON, BUSAN, DAEGU, GWANGJU, DAEJEON, ULSAN, SEJONG, GYEONGSANGNAMDO, GYEONGSANGBUKDO,
        JEOLLANAMDO, JEOLLABUKDO, CHUNGCHEONGNAMDO, CHUNGCHEONGBUKDO, GANGWONDO, JEJUDO
    }
}
