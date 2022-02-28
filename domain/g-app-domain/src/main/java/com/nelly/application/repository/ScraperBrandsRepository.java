package com.nelly.application.repository;

import com.nelly.application.domain.ScraperBrands;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScraperBrandsRepository extends JpaRepository<ScraperBrands, Long> {
}
