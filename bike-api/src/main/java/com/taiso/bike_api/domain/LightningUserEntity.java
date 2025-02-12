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

@Entity
//번개와 유저 조합으로 유니크 제약조건 추가
@Table(name = "lightning_user",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"lightning_id", "user_id"})
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LightningUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lightning_user_id")
    private Long lightningUserId;

    /**
     * 참가 상태 ENUM (예: 신청대기, 승인, 탈퇴, 완료)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "participant_status", nullable = false)
    private ParticipantStatus participantStatus;

    /**
     * 참여 신청 일시: 생성 시점에 자동 설정
     */
    @CreationTimestamp
    @Column(name = "joined_at", nullable = false, updatable = false)
    private LocalDateTime joinedAt;

    /**
     * 역할 ENUM (예: 번개생성자, 참여자)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    /**
     * 선택적 연관관계: Lightning 엔티티
     * 복합 키의 일부이므로 insert, update 시에는 무시
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lightning_id", insertable = false, updatable = false)
    private LightningEntity lightning;

    /**
     * 선택적 연관관계: User 엔티티
     * 복합 키의 일부이므로 insert, update 시에는 무시
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserEntity user;

    /**
     * 참가 상태 ENUM 정의
     * 값: 신청대기, 승인, 탈퇴, 완료
     */
    public enum ParticipantStatus {
        신청대기,
        승인,
        탈퇴,
        완료
    }

    /**
     * 역할 ENUM 정의
     * 값: 번개생성자, 참여자
     */
    public enum Role {
        번개생성자,
        참여자
    }
}
