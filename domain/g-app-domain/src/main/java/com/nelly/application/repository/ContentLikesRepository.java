package com.nelly.application.repository;

import com.nelly.application.domain.ContentLikes;
import com.nelly.application.domain.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContentLikesRepository extends JpaRepository<ContentLikes, Long> {
    Optional<ContentLikes> findByContentIdAndAndUserId(Long contentId, Long userId);
    Page<ContentLikes> findAllByContentId(Long contentId, Pageable pageable);
    Page<ContentLikes> findAllByContent_UserAndContent_DeletedDateNull(Users user, Pageable pageable);
}
