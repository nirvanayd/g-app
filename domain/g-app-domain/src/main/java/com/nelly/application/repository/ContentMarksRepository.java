package com.nelly.application.repository;

import com.nelly.application.domain.ContentMarks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ContentMarksRepository extends JpaRepository<ContentMarks, Long> {
    Optional<ContentMarks> findByContentIdAndAndUserId(Long contentId, Long userId);
}
