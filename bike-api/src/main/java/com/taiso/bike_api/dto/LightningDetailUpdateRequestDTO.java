package com.taiso.bike_api.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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

    private String status;

    private String gender;
    private String level;
    private String recruitType;
    private String bikeType;
    private String region;

    private Long tagId;
    private Set<String> lightningTag;

}
