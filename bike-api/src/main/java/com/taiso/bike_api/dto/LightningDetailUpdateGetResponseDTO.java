package com.taiso.bike_api.dto;

import com.taiso.bike_api.domain.LightningEntity;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LightningDetailUpdateGetResponseDTO {

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
