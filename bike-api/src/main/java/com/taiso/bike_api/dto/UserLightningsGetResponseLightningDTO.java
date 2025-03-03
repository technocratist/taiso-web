package com.taiso.bike_api.dto;

import java.time.LocalDateTime;

import com.taiso.bike_api.domain.LightningEntity.LightningStatus;

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
public class UserLightningsGetResponseLightningDTO {
    private Long lightningId;
    private String title;
    private LocalDateTime eventDate;
    private Long creatorId;
    private LightningStatus status;
    private Integer duration;
    private Integer capacity;
    private String address;
}
