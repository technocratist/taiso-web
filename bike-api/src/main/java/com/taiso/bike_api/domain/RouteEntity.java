package com.taiso.bike_api.domain;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "route")
public class RouteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id")
    private Long routeId;

    /**
     * 루트 이름
     */
    @Column(name = "route_name", nullable = false, length = 255)
    private String routeName;

    /**
     * 루트 설명
     */
    @Column(name = "description", length = 1000)
    private String description;

    /**
     * 루트 이미지 ID
     */
    @Column(name = "route_img_id")
    private Integer routeImgId;

    /**
     * 유저 ID
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 좋아요 수
     */
    @Column(name = "like_count", columnDefinition = "BIGINT default 0")
    private Long likeCount;

    /**
     * 생성 일시
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 수정 일시
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 지역
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "region", nullable = false, length = 20)
    private Region region;

    /**
     * 거리
     */
    @Column(name = "distance", nullable = false, precision = 10, scale = 2)
    private BigDecimal distance;

    /**
     * 고도
     */
    @Column(name = "altitude", nullable = false, precision = 10, scale = 2)
    private BigDecimal altitude;

    /**
     * 거리 단위
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "distance_type", nullable = false, length = 20)
    private DistanceType distanceType;

    /**
     * 고도 단위
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "altitude_type", nullable = false, length = 20)
    private AltitudeType altitudeType;

    /**
     * 도로 유형
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "road_type", nullable = false, length = 20)
    private RoadType roadType;

    /**
     * 원본 파일 경로
     */
    @Column(name = "original_file_path", length = 255)
    private String originalFilePath;

    /**
     * 파일 이름
     */
    @Column(name = "file_name", length = 255)
    private String fileName;

    /**
     * 파일 유형
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "file_type", length = 10)
    private FileType fileType;

    // Route와 Tag 간 다대다 관계 (연결 테이블은 route_tag)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "route_tag",
            joinColumns = @JoinColumn(name = "route_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<RouteTagCategoryEntity> tags = new HashSet<>();

    //빌더
    public static RouteEntityBuilder builder() {
        return new RouteEntityBuilder();
    }    
    
    enum Region {
        서울,
        경기,
        인천,
        강원,
        충청,
        전라,
        경상,
        제주;
    }
    
    enum DistanceType {
        킬로미터,
        마일;
    }
    
    
    enum AltitudeType {
        미터,
        피트;
    }
    
    
    enum RoadType {
        평지,
        산길,
        고속도로;
    }
    
    
    enum FileType {
        GPX,
        TCX;
    }

}    
