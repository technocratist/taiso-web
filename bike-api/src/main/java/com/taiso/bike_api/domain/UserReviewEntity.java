package com.taiso.bike_api.domain;

import java.time.LocalDateTime;

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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "user_review")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserReviewEntity {
        /**
     * 고유 리뷰 ID (surrogate key)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    /**
     * 리뷰 작성 유저 (외래키: users.user_id)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private UserEntity reviewer;

    /**
     * 리뷰 대상 유저 (외래키: users.user_id)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_id", nullable = false)
    private UserEntity reviewed;

    /**
     * 리뷰 내용 (선택적, 최대 500자)
     */
    @Column(name = "review_content", length = 500)
    private String reviewContent;

    /**
     * 해당 리뷰가 속한 번개 (외래키: lightning.lightning_id)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lightning_id", nullable = false)
    private LightningEntity lightning;

    /**
     * 리뷰용 태그 (ENUM, 예: EXCELLENT, GOOD, AVERAGE, POOR)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "review_tag", nullable = false)
    private ReviewTag reviewTag;

    /**
     * 상태 생성 시간 (레코드 생성 시 자동 설정)
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 마지막 수정 시간 (레코드 수정 시 자동 업데이트)
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    //빌더
    public static UserReviewEntityBuilder builder() {
        return new UserReviewEntityBuilder();
    }
    /**
     * 리뷰용 태그 ENUM
     * 실제 값은 비즈니스 로직에 따라 수정 가능
     */
    public enum ReviewTag {
        EXCELLENT,
        GOOD,
        AVERAGE,
        POOR
    }
}
