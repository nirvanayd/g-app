package com.nelly.application.repository;

import com.nelly.application.domain.ScrapItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScrapItemsRepository extends JpaRepository<ScrapItems, Long> {
}
