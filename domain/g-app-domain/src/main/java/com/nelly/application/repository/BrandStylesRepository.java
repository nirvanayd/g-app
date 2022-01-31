package com.nelly.application.repository;

import com.nelly.application.domain.BrandStyles;
import com.nelly.application.domain.Brands;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandStylesRepository extends JpaRepository<BrandStyles, Long> {
}
