package com.nelly.application.repository;

import com.nelly.application.domain.BrandRank;
import com.nelly.application.domain.Brands;
import com.nelly.application.enums.BrandStatus;
import com.nelly.application.enums.DisplayType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRankRepository extends JpaRepository<BrandRank, Long> {
    Page<BrandRank> findAll(Pageable pageable);
}
