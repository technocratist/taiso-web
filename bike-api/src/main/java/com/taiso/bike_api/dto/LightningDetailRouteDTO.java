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
public class LightningDetailRouteDTO {

    private Long routeId;
    private String routeName;
    private String routeImgId;

    private String originalFilePath;
    private String fileName;
    private String fileType;

    private List<RoutePointDTO> routePoints;

}
