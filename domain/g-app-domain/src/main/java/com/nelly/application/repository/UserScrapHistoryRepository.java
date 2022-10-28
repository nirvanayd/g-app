package com.nelly.application.repository;

import com.nelly.application.domain.ScrapItems;
import com.nelly.application.domain.UserScrapCart;
import com.nelly.application.domain.UserScrapHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserScrapHistoryRepository extends JpaRepository<UserScrapHistory, Long> {
    Optional<UserScrapHistory> findByUserIdAndScrapItem(long userId, ScrapItems scrapItem);
    Page<UserScrapHistory> findAllByUserId(long userId, Pageable pageable);
}
