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
public class LightningDetailGetResponseDTO {

    private Long lightningId;
    private Long creatorId;

    private String title;
    private String description;

    private LocalDateTime eventDate;
    private Integer duration;

    private LocalDateTime createdAt;
//  private LocalDateTime updatedAt;

    private String status;
    private Integer capacity;

    private BigDecimal latitude;
    private BigDecimal longitude;

    private String gender;
    private String level;
    private String bikeType;
    private String region;

    private String recruitType;

    private Long distance;
    private Long routeId;
    private String address;

    // 클럽 관련
    private Boolean isClubOnly;
    private Long clubId;

    // 번개 참여자
    private Long lightningUserId;
//    List<LightningDetailMemberDTO> member;

    // 번개 태그
    private Long tagId;
    private List<String> lightningTag;




}
