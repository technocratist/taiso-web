package com.taiso.bike_api.dto;

import com.taiso.bike_api.domain.RouteEntity;
import lombok.*;

import java.util.List;

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
