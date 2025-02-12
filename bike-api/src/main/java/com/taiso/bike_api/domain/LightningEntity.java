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
@Table(name = "lightning")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LightningEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lightning_id")
    private Long lightningId;

    @Column(name = "creator_id", nullable = false)
    private Long creatorId;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    // createdAt은 등록시 자동 세팅되도록 함 (JPA Auditing 또는 @PrePersist 사용)
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 수정 시 자동 업데이트되도록 함
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LightningStatus status;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Column(name = "latitude", nullable = false, precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 9, scale = 6)
    private BigDecimal longitude;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    private Level level;

    @Enumerated(EnumType.STRING)
    @Column(name = "recruit_type", nullable = false)
    private RecruitType recruitType;

    @Enumerated(EnumType.STRING)
    @Column(name = "bike_type", nullable = false)
    private BikeType bikeType;

    @Enumerated(EnumType.STRING)
    @Column(name = "region", nullable = false)
    private Region region;

    @Column(name = "distance", nullable = false)
    private Long distance;

    @Column(name = "route_id")
    private Long routeId;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "is_club_only")
    private Boolean isClubOnly;

    @Column(name = "club_id")
    private Long clubId;

    @ManyToMany
    @JoinTable(name = "lightning_tag", joinColumns = @JoinColumn(name = "lightning_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<LightningTagCategoryEntity> tags = new HashSet<>();

    //빌더
    public static LightningEntityBuilder builder() {
        return new LightningEntityBuilder();
    }

    // ENUM 정의

    /**
     * 번개 상태 ENUM
     * 값: 모집, 마감, 종료, 취소
     */
    public enum LightningStatus {
        모집, 마감, 종료, 취소
    }

    /**
     * 참여자 성별 ENUM
     * 값: 남, 여, 자유
     */
    public enum Gender {
        남, 여, 자유
    }

    /**
     * 번개 수준 ENUM
     * 값: 초보, 중급, 고급, 자유
     */
    public enum Level {
        초보, 중급, 고급, 자유
    }

    /**
     * 모집 방식 ENUM
     * 값: 참가형, 수락형
     */
    public enum RecruitType {
        참가형, 수락형
    }

    /**
     * 자전거 타입 ENUM
     * 값: 로드, 따릉이, 하이브리드, 자유
     */
    public enum BikeType {
        로드, 따릉이, 하이브리드, 자유
    }

    /**
     * 지역 ENUM
     * 값: 서울, 경기, 대구, 강원
     */
    public enum Region {
        서울, 경기, 대구, 강원
    }
}