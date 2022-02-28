package com.nelly.application.repository;

import com.nelly.application.domain.ScraperSimulationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScraperSimulationLogRepository extends JpaRepository<ScraperSimulationLog, Long> {
}
