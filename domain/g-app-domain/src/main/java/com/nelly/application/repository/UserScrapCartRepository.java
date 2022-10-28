package com.nelly.application.repository;

import com.nelly.application.domain.ScrapItems;
import com.nelly.application.domain.UserScrapCart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserScrapCartRepository extends JpaRepository<UserScrapCart, Long> {
    Optional<UserScrapCart> findByUserIdAndScrapItem(long userId, ScrapItems scrapItem);
    Page<UserScrapCart> findAllByUserId(long userId, Pageable pageable);
}
