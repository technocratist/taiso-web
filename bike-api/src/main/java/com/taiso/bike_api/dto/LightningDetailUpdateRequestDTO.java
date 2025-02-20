package com.taiso.bike_api.dto;

import java.util.List;

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
public class LightningDetailUpdateRequestDTO {

    private Long lightningId;
    private Long creatorId;

    private String title;
    private String description;
//    private LocalDateTime eventDate;
//    private Integer duration;

    private String status;
//    private Integer capacity;

//    private BigDecimal latitude;
//    private BigDecimal longitude;

    private String gender;
    private String level;
    private String recruitType;
    private String bikeType;
    private String region;

//    private Long distance;
//    private Long routeId;
//    private String address;

    private Long tagId;
    private List<String> lightningTag;

}
