package com.taiso.bike_api;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.taiso.bike_api.domain.ClubEntity;
import com.taiso.bike_api.domain.ClubMemberEntity;
import com.taiso.bike_api.domain.LightningEntity;
import com.taiso.bike_api.domain.LightningUserEntity;
import com.taiso.bike_api.domain.RouteEntity;
import com.taiso.bike_api.domain.RoutePointEntity;
import com.taiso.bike_api.domain.UserEntity;
import com.taiso.bike_api.domain.UserRoleEntity;
import com.taiso.bike_api.domain.UserStatusEntity;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;


// 테스트 시 실제 DB 대신 H2와 같은 인메모리 DB가 사용된다고 가정
@SpringBootTest
@Transactional
public class EntityIntegrationTest {

    @Autowired
    private EntityManager em;
    
    /**
     * 유저가 번개(자전거 모임 이벤트)를 생성하고 본인이 참가하는 상황을 테스트
     */
    @Test
    public void testLightningCreationAndParticipation() {
        // -- 사용자 생성에 필요한 기본 엔티티 (UserRole, UserStatus) 생성
        UserRoleEntity role = UserRoleEntity.builder()
                .roleName("1")
                .build();
        em.persist(role);
        
        UserStatusEntity status = UserStatusEntity.builder()
                .statusName("1")
                .build();
        em.persist(status);
        
        UserEntity creator = UserEntity.builder()
                .email("creator@example.com")
                .password("secret")
                .role(role)
                .status(status)
                .build();
        em.persist(creator);
        em.flush();  // creator.userId 값 생성
        
        // -- 번개 이벤트 생성 (creator의 userId를 creatorId로 지정)
        LightningEntity lightning = LightningEntity.builder()
                .creatorId(creator.getUserId())
                .title("Morning Ride")
                .description("A refreshing morning ride event")
                .eventDate(LocalDateTime.now().plusDays(1))
                .duration(120)
                .status(LightningEntity.LightningStatus.모집)
                .capacity(10)
                .latitude(new BigDecimal("37.123456"))
                .longitude(new BigDecimal("127.123456"))
                .gender(LightningEntity.Gender.자유)
                .level(LightningEntity.Level.중급)
                .recruitType(LightningEntity.RecruitType.참가형)
                .bikeType(LightningEntity.BikeType.자유)
                .region(LightningEntity.Region.서울)
                .distance(5000L)
                .address("Seoul, Korea")
                .isClubOnly(false)
                .build();
        em.persist(lightning);
        
        // -- 번개 이벤트 참여(참여자 등록)
        LightningUserEntity lightningUser = LightningUserEntity.builder()
                .participantStatus(LightningUserEntity.ParticipantStatus.신청대기)
                .role(LightningUserEntity.Role.번개생성자)
                .lightning(lightning)
                .user(creator)
                .build();
        em.persist(lightningUser);
        em.flush();
        
        // -- 검증: 번개 이벤트가 정상 생성되었고, 제목이 일치하는지 확인
        LightningEntity foundLightning = em.find(LightningEntity.class, lightning.getLightningId());
        assertNotNull(foundLightning);
        assertEquals("Morning Ride", foundLightning.getTitle());
    }
    
    /**
     * 클럽을 생성하고, 다른 사용자가 클럽에 가입하는 상황을 테스트
     */
    @Test
    public void testClubCreationAndMemberParticipation() {
        // -- 기본 사용자 및 역할/상태 엔티티 생성
        UserRoleEntity role = UserRoleEntity.builder()
                .roleName("1")
                .build();
        em.persist(role);
        
        UserStatusEntity status = UserStatusEntity.builder()
                .statusName("1")
                .build();
        em.persist(status);
        
        // 클럽장 (리더) 생성
        UserEntity leader = UserEntity.builder()
                .email("leader@example.com")
                .password("secret")
                .role(role)
                .status(status)
                .build();
        em.persist(leader);
        
        // 일반 회원 생성
        UserEntity member = UserEntity.builder()
                .email("member@example.com")
                .password("secret")
                .role(role)
                .status(status)
                .build();
        em.persist(member);
        em.flush();
        
        // -- 클럽 생성 (클럽장 지정)
        ClubEntity club = ClubEntity.builder()
                .clubName("Biking Club")
                .clubLeader(leader)
                .clubShortDescription("A club for biking enthusiasts")
                .clubDescription("We love biking and exploring new routes!")
                .maxUser(20)
                .build();
        em.persist(club);
        
        // -- 클럽 멤버 등록 (일반 회원이 클럽에 가입 신청)
        ClubMemberEntity clubMember = ClubMemberEntity.builder()
                .user(member)
                .club(club)
                .role(ClubMemberEntity.Role.멤버)
                .participantStatus(ClubMemberEntity.ParticipantStatus.신청대기)
                .build();
        em.persist(clubMember);
        em.flush();
        
        // -- 검증: 클럽이 정상 생성되었고, 클럽명이 일치하는지 확인
        ClubEntity foundClub = em.find(ClubEntity.class, club.getClubId());
        assertNotNull(foundClub);
        assertEquals("Biking Club", foundClub.getClubName());
    }
    
    /**
     * 유저가 루트를 생성하고, 해당 루트에 여러 포인트(route_point)를 등록한 후 조회하는 상황을 테스트
     */
    @Test
    public void testRouteCreationAndView() {
        // -- 기본 사용자 생성 (루트의 소유자)
        UserRoleEntity role = UserRoleEntity.builder()
                .roleName("1")
                .build();
        em.persist(role);
        UserStatusEntity status = UserStatusEntity.builder()
                .statusName("1")
                .build();
        em.persist(status);
        
        UserEntity user = UserEntity.builder()
                .email("user@example.com")
                .password("secret")
                .role(role)
                .status(status)
                .build();
        em.persist(user);
        em.flush();
        
        // -- 루트 생성 (소유자는 단순 userId 값으로 관리)
        RouteEntity route = RouteEntity.builder()
                .routeName("Scenic Trail")
                .description("A scenic route through the mountains")
                .userId(user.getUserId())
                .likeCount(0L)
                .region(RouteEntity.Region.강원)
                .distance(new BigDecimal("50.00"))
                .altitude(new BigDecimal("1200.00"))
                .distanceType(RouteEntity.DistanceType.킬로미터)
                .altitudeType(RouteEntity.AltitudeType.미터)
                .roadType(RouteEntity.RoadType.산길)
                .originalFilePath("/files/route1.gpx")
                .fileName("route1.gpx")
                .fileType(RouteEntity.FileType.GPX)
                .build();
        em.persist(route);
        
        // -- 루트 경로 상의 포인트들 등록
        RoutePointEntity point1 = RoutePointEntity.builder()
                .route(route)
                .sequence(1)
                .latitude(new BigDecimal("37.111111"))
                .longitude(new BigDecimal("127.111111"))
                .elevation(new BigDecimal("50.0"))
                .build();
        em.persist(point1);
        
        RoutePointEntity point2 = RoutePointEntity.builder()
                .route(route)
                .sequence(2)
                .latitude(new BigDecimal("37.222222"))
                .longitude(new BigDecimal("127.222222"))
                .elevation(new BigDecimal("55.0"))
                .build();
        em.persist(point2);
        em.flush();
        
        // -- 검증: 루트가 정상 생성되었고, 루트명과 등록된 포인트의 개수를 확인
        RouteEntity foundRoute = em.find(RouteEntity.class, route.getRouteId());
        assertNotNull(foundRoute);
        assertEquals("Scenic Trail", foundRoute.getRouteName());
        
        // 추가로 RoutePointEntity는 루트와 다대일 관계이므로, JPQL이나 연관관계 조회로 포인트 개수를 확인할 수 있음
        Long pointCount = em.createQuery("SELECT COUNT(rp) FROM RoutePointEntity rp WHERE rp.route = :route", Long.class)
                            .setParameter("route", foundRoute)
                            .getSingleResult();
        assertEquals(2L, pointCount);
    }
}