package com.taiso.bike_api.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoutePostRequestDTO {

    @NotBlank
    private String routeName;
    
    @NotNull
    private Long userId;
    
    @NotBlank
    private String description; // Note: using "description" over "discription"
    
    @NotNull
    private List<String> tag;
    
    @NotBlank
    private String region;
    
    @NotBlank
    private String distanceType;
    
    @NotBlank
    private String altitudeType;
    
    @NotBlank
    private String roadType;
}
