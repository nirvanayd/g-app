package com.nelly.application.repository;

import com.nelly.application.domain.ScraperLog;
import com.nelly.application.domain.ScraperRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScraperRequestRepository extends JpaRepository<ScraperRequest, Long> {
}
