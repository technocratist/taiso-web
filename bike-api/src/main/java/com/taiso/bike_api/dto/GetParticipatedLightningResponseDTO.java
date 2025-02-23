package com.taiso.bike_api.dto;

import java.time.LocalDateTime;

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
public class GetParticipatedLightningResponseDTO {
    private Long lightningId;

    private Long creatorId;

    private String title;

    private LocalDateTime eventDate;

    private Integer duration;

    private LocalDateTime createdAt;

    private BookmarkedMemberResponseDTO bookmarkedMember;
}
