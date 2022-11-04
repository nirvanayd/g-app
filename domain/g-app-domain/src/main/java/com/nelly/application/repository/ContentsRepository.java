package com.nelly.application.repository;

import com.nelly.application.domain.Contents;
import com.nelly.application.domain.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ContentsRepository extends JpaRepository<Contents, Long> {
    Optional<Contents> findByUserAndId(Users user, Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Contents c SET c.likeCount = c.likeCount + :value WHERE c.id = :contentId")
    void updateLikeCount(@Param("contentId") Long contentId, @Param("value") int value);

    @Transactional
    @Modifying
    @Query("UPDATE Contents c SET c.markCount = c.markCount + :value WHERE c.id = :contentId")
    void updateMarkCount(@Param("contentId") Long contentId, @Param("value") int value);

    @Transactional
    @Modifying
    @Query("UPDATE Contents c SET c.replyCount = c.replyCount + :value WHERE c.id = :contentId")
    void updateContentReply(@Param("contentId") Long contentId, @Param("value") int value);

    Page<Contents> findAllByUser(Users user, Pageable pageable);

    @Query("SELECT COALESCE(SUM(c.likeCount),0) FROM Contents c WHERE c.deletedDate IS NULL")
    long countUserLike(@Param("user") Users user);

    @Query("SELECT COALESCE(SUM(c.markCount),0) FROM Contents c WHERE c.deletedDate IS NULL")
    long countUserMark(@Param("detailUser") Users detailUser);

    Page<Contents> findAllByItemHashTags_AppTag_Id(long id, Pageable pageable);
}
