package com.taiso.bike_api.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class BookmarkedMemberResponseDTO {
    private Long userId;

    private String userName;

    private LocalDateTime joinedAt;
}
