package com.taiso.bike_api.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.taiso.bike_api.domain.RouteEntity;
import com.taiso.bike_api.dto.RouteRequestDTO;
import com.taiso.bike_api.service.RouteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/routes")
@Tag(name = "루트 컨트롤러", description = "루트 관련 API")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @PostMapping(value="/test", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "루트 생성", description = "루트를 생성하는 API")
    public ResponseEntity<Map<String, Object>> createRoute(
            @RequestPart(value = "routeData") RouteRequestDTO routeData,
            @RequestPart(value = "file") MultipartFile file) {

        log.info("RouteController.createRoute() 호출됨");
        log.info("routeData: {}", routeData);
        log.info("file: {}", file);

        RouteEntity savedRoute = routeService.createRoute(routeData, file);
        Map<String, Object> response = new HashMap<>();
        response.put("routeId", savedRoute.getRouteId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }



}


