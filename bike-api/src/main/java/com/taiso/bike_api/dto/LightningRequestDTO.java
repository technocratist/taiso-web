package com.taiso.bike_api.dto;

import java.time.LocalDateTime;

public class LightningRequestDTO {
    private Long lightningId;
    
    private String title;

    private String description;
    
    private LocalDateTime eventDate;

    private Integer duration;

    private String status;

    private Integer capacity;

    private Double latitude;

    private Double longitude;

    private String address;

    private Gender gender;

    private Level level;

    private RecruitType RecruitType;

    private Long creatorId;

    private BikeType bikeType;

    private Region region;

    private Integer distance;

    private Long routeId;

    // private 

    public enum Gender {
        남성,
        여성,
        선택안함
    }

    public enum Level {
        자유,
        초보,
        중급,
        고급
    }

    public enum RecruitType {
        참가형,
        주최형
    }

    public enum BikeType {
        로드,
        산악
    }

    public enum Region {
        강남구,
        강동구,
        강서구,
        강북구,
        마포구,
        광진구,
        은평구,
        관악구,
        서초구,
        동작구,
        종로구,
        용산구,
        중구,
        송파구,
        구로구,
        중랑구,
        도봉구,
        영등포구,
        양천구,
        금천구,
        성동구,
        서대문구,
        노원구,
        동대문구,
        성북구
    }
}
