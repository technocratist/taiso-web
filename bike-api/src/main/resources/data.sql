-- 1. user_role 테이블에 역할 추가
INSERT INTO user_role (role_name, created_at, updated_at)
VALUES ('USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 2. user_status 테이블에 상태 추가
INSERT INTO user_status (status_name, created_at, updated_at)
VALUES ('ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 3. route_tag 테이블에 태그 추가
INSERT INTO route_tag_category (name, created_at, updated_at)
VALUES ('한강 자전거길', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO route_tag_category (name, created_at, updated_at)
VALUES ('국토종주길', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO route_tag_category (name, created_at, updated_at)
VALUES ('산악 도로', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO route_tag_category (name, created_at, updated_at)
VALUES ('평지 도로', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO route_tag_category (name, created_at, updated_at)
VALUES ('위험한 공도', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 4. lightning_tag 테이블에 태그 추가
INSERT INTO lightning_tag_category (name, created_at, updated_at)
VALUES ('샤방법', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO lightning_tag_category (name, created_at, updated_at)
VALUES ('오픈라이딩', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO lightning_tag_category (name, created_at, updated_at)
VALUES ('따폭연', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO lightning_tag_category (name, created_at, updated_at)
VALUES ('장거리', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO lightning_tag_category (name, created_at, updated_at)
VALUES ('따릉이', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO lightning_tag_category (name, created_at, updated_at)
VALUES ('친목', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);