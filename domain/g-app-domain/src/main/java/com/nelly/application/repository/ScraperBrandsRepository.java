package com.nelly.application.repository;

import com.nelly.application.domain.ScraperBrands;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScraperBrandsRepository extends JpaRepository<ScraperBrands, Long> {
    List<ScraperBrands> findByStoreUrlContains(String host);
    Optional<ScraperBrands> findByNameContains(String str);
}
