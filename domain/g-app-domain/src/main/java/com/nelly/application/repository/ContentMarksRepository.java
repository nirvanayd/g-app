package com.nelly.application.repository;

import com.nelly.application.domain.ContentMarks;
import com.nelly.application.domain.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContentMarksRepository extends JpaRepository<ContentMarks, Long> {
    Optional<ContentMarks> findByContentIdAndAndUserId(Long contentId, Long userId);
    Page<ContentMarks> findAllByContentId(Long contentId, Pageable pageable);
    Page<ContentMarks> findAllByUserId(Long userId, Pageable pageable);
    Page<ContentMarks> findAllByUser(Users user, Pageable pageable);
    Page<ContentMarks> findAllByContent_User(Users user, Pageable pageRequest);
}
