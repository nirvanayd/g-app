package com.nelly.application.repository;

import com.nelly.application.domain.Contents;
import com.nelly.application.domain.Users;
import com.nelly.application.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ContentsRepository extends JpaRepository<Contents, Long> {
    Optional<Contents> findByUserAndId(Users user, Long id);

    Page<Contents> findAllByIsDisplay(int isDisplay, Pageable pageable);

    Page<Contents> findAllByIsDisplayAndUser_Status(int isDisplay, UserStatus userStatus, Pageable pageable);

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
    Page<Contents> findAllByUserAndIsDisplay(Users user, int isDisplay, Pageable pageable);

    @Query("SELECT COALESCE(SUM(c.likeCount),0) FROM Contents c WHERE c.deletedDate IS NULL AND c.user = :user")
    long countUserLike(@Param("user") Users user);

    @Query("SELECT COALESCE(SUM(c.markCount),0) FROM Contents c WHERE c.deletedDate IS NULL AND c.user = :detailUser")
    long countUserMark(@Param("detailUser") Users detailUser);

    Page<Contents> findAllByItemHashTags_AppTag_Id(long id, Pageable pageable);

    @Query("SELECT COALESCE(COUNT(c.id),0) FROM Contents c WHERE c.deletedDate IS NULL AND c.user = :user AND c.isDisplay = 0")
    long countBlockContentCount(@Param("user") Users user);

    List<Contents> findAllByIsDisplay(int isDisplay);
}
