package com.taiso.bike_api.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity
@Table(name = "lightning_tag_category")
@NoArgsConstructor
@AllArgsConstructor
public class LightningTagCategoryEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long tagId;

    /**
     * 태그 이름
     */
    @Column(name = "name", nullable = false, unique = true, length = 255)
    private String name;

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
     * 번개 엔티티와 양방향 관계 설정
     */
    @ManyToMany(mappedBy = "tags")
    private Set<LightningEntity> lightning = new HashSet<>();

    //빌더
    public static LightningTagCategoryEntityBuilder builder() {
        return new LightningTagCategoryEntityBuilder();
    }
}
