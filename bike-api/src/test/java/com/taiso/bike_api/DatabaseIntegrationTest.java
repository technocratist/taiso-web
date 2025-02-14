package com.taiso.bike_api;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.taiso.bike_api.domain.BookmarkEntity;
import com.taiso.bike_api.domain.ClubBoardEntity;
import com.taiso.bike_api.domain.ClubEntity;
import com.taiso.bike_api.domain.ClubMemberEntity;
import com.taiso.bike_api.domain.UserEntity;
import com.taiso.bike_api.domain.UserRoleEntity;
import com.taiso.bike_api.domain.UserStatusEntity;
import com.taiso.bike_api.repository.BookmarkRepository;
import com.taiso.bike_api.repository.ClubBoardRepository;
import com.taiso.bike_api.repository.ClubMemberRepository;
import com.taiso.bike_api.repository.ClubRepository;
import com.taiso.bike_api.repository.UserRepository;
import com.taiso.bike_api.repository.UserRoleRepository;
import com.taiso.bike_api.repository.UserStatusRepository;

@SpringBootTest
@Transactional
class DatabaseIntegrationTest {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserStatusRepository userStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private ClubBoardRepository clubBoardRepository;

    @Autowired
    private ClubMemberRepository clubMemberRepository;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Test
    void contextLoads() {
        // Context load 테스트 (스프링 컨텍스트가 정상적으로 로드되는지 확인)
    }

    @Test
    void testEntitiesCRUD() {
        // 1. 유저 생성에 필요한 Role과 Status 생성
        UserRoleEntity role = UserRoleEntity.builder()
                .roleName("ROLE_USER")
                .build();
        role = userRoleRepository.save(role);

        UserStatusEntity status = UserStatusEntity.builder()
                .statusName("a")
                .build();
        status = userStatusRepository.save(status);

        // 2. User 생성
        UserEntity user = UserEntity.builder()
                .email("test@example.com")
                .password("encodedPassword")
                .role(role)
                .status(status)
                .build();
        user = userRepository.save(user);
        assertNotNull(user.getUserId(), "User ID should not be null after saving.");

        // 3. Club 생성 (클럽의 리더로 위에서 생성한 user 사용)
        ClubEntity club = ClubEntity.builder()
                .clubName("Test Club")
                .clubLeader(user)
                .clubShortDescription("This is a short club description.")
                .clubDescription("This is a longer description about the club.")
                .maxUser(50)
                .build();
        club = clubRepository.save(club);
        assertNotNull(club.getClubId(), "Club ID should not be null after saving.");

        // 4. ClubBoard (클럽 게시글) 생성
        ClubBoardEntity board = ClubBoardEntity.builder()
                .club(club)
                .postWriter(user)
                .postTitle("Test Post Title")
                .postContent("This is the content of the test post.")
                .isNotice(false)
                .build();
        board = clubBoardRepository.save(board);
        assertNotNull(board.getPostId(), "Post ID should not be null after saving.");

        // 5. ClubMember 생성 (user가 club의 멤버로 가입)
        ClubMemberEntity member = ClubMemberEntity.builder()
                .club(club)
                .user(user)
                .role(ClubMemberEntity.Role.멤버)
                .participantStatus(ClubMemberEntity.ParticipantStatus.승인)
                .build();
        member = clubMemberRepository.save(member);
        assertNotNull(member.getMemberId(), "Member ID should not be null after saving.");

        // 6. Bookmark 생성 (user가 club을 북마크)
        BookmarkEntity bookmark = BookmarkEntity.builder()
                .user(user)
                .targetType(BookmarkEntity.BookmarkType.CLUB)
                .targetId(club.getClubId())
                .build();
        bookmark = bookmarkRepository.save(bookmark);
        assertNotNull(bookmark.getBookmarkId(), "Bookmark ID should not be null after saving.");

        // 7. 저장된 북마크 조회 및 검증
        Optional<BookmarkEntity> foundBookmark = bookmarkRepository.findById(bookmark.getBookmarkId());
        assertTrue(foundBookmark.isPresent(), "Bookmark should be found by its ID.");
        assertEquals(BookmarkEntity.BookmarkType.CLUB, foundBookmark.get().getTargetType(), "Bookmark type should be CLUB.");
        assertEquals(club.getClubId(), foundBookmark.get().getTargetId(), "Bookmark target ID should match Club ID.");
    }
}