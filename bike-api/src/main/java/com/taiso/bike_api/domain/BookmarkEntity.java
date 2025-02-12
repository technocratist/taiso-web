package com.taiso.bike_api.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 북마크 엔티티
 * 사용자가 다양한 대상(유저, 번개, 클럽, 루트)을 북마크할 수 있도록 함.
 */
@Entity
@Table(name = "bookmark", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "target_type", "target_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookmarkEntity {

    /**
     * 북마크 고유 식별자
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
    private Long bookmarkId;

    /**
     * 북마크를 등록한 사용자 (users 테이블과 연관)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    /**
     * 북마크 대상의 종류 (USER, LIGHTNING, CLUB, ROUTE)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false, length = 20)
    private BookmarkType targetType;

    /**
     * 북마크 대상의 식별자 (대상 테이블의 PK 값)
     */
    @Column(name = "target_id", nullable = false)
    private Long targetId;

    /**
     * 북마크 등록 일시
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 북마크 대상의 종류를 나타내는 ENUM
     */
    public enum BookmarkType {
        USER,       // 유저 북마크 (다른 사용자를 즐겨찾기)
        LIGHTNING,  // 번개 북마크 (번개 이벤트)
        CLUB,       // 클럽 북마크
        ROUTE       // 루트 북마크
    }
}