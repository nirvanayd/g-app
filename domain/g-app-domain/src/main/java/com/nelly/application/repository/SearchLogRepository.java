package com.nelly.application.repository;

import com.nelly.application.domain.ScrapItems;
import com.nelly.application.domain.SearchLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SearchLogRepository extends JpaRepository<SearchLog, Long> {
    Optional<SearchLog> findByUserIdAndKeyword(long userId, String keyword);
    Page<SearchLog> findAllByUserId(long userId, Pageable pageable);
}
