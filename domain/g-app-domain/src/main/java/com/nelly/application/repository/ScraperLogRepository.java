package com.nelly.application.repository;

import com.nelly.application.domain.ScraperLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScraperLogRepository extends JpaRepository<ScraperLog, Long> {
}
