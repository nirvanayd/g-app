package com.nelly.application.repository;

import com.nelly.application.domain.ScrapItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScrapItemsRepository extends JpaRepository<ScrapItems, Long> {
    Optional<ScrapItems> findByUrl(String url);
}
