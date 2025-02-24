package com.taiso.bike_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taiso.bike_api.domain.BookmarkEntity;
import com.taiso.bike_api.domain.BookmarkEntity.BookmarkType;

@Repository
public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Long> {

    Optional<BookmarkEntity> findByUserIdAndTargetTypeAndTargetId(Long userId, BookmarkType lightning,
            Long lightningId);

}
