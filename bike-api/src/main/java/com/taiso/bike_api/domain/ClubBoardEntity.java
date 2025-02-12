package com.taiso.bike_api.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "club_board")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubBoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    /**
     * 해당 게시글이 속한 클럽 (club 테이블의 club_id를 참조)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private ClubEntity club;

    /**
     * 게시글 작성자 (users 테이블의 user_id를 참조)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_writer_id", nullable = false)
    private UserEntity postWriter;

    /**
     * 게시글 제목
     */
    @Column(name = "post_title", nullable = false)
    private String postTitle;

    /**
     * 게시글 내용 (길이가 길어질 수 있으므로 TEXT 타입 지정)
     */
    @Column(name = "post_content", columnDefinition = "TEXT")
    private String postContent;

    /**
     * 작성일 (자동 설정)
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 수정 시각 (자동 업데이트)
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 공지글 여부
     */
    @Column(name = "is_notice", nullable = false)
    private Boolean isNotice;
}