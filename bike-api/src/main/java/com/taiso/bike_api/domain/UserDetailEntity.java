package com.taiso.bike_api.domain;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user_detail")
public class UserDetailEntity {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "user_nickname", length = 50, unique = true)
    private String userNickname;

    // 이미지 번호는 별도의 AUTO_INCREMENT 없이 기본값 null로 관리
    @Column(name = "user_profile_img")
    private String userProfileImg;

    @Column(name = "user_background_img")
    private String userBackgroundImg;


    @Column(name = "full_name", length = 500)
    private String fullName;

    @Column(name = "phone_number", length = 50)
    private String phoneNumber;

    @Column(name = "birth_date")
    private LocalDateTime birthDate;

    @Column(name = "bio", length = 500)
    private String bio;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 10)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", length = 20)
    private Level level;

    @Column(name = "FTP")
    private Integer FTP;

    @Column(name = "height")
    private Integer height;

    @Column(name = "weight")
    private Integer weight;

    public enum Gender {
        남자, 여자, 그외
    }

    public enum Level {
        무경력, 초보자, 입문자, 중수, 고수
    }
}