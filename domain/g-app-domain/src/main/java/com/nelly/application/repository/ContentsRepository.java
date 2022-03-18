package com.nelly.application.repository;

import com.nelly.application.domain.Contents;
import com.nelly.application.domain.Users;
import org.springframework.data.domain.Page;
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

}
