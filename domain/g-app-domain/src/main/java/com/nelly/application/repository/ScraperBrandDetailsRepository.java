package com.nelly.application.repository;

import com.nelly.application.domain.ScraperBrandDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScraperBrandDetailsRepository extends JpaRepository<ScraperBrandDetails, Long> {
    List<ScraperBrandDetails> findAllByHost(String host);

}
