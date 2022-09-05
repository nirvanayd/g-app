package com.nelly.application.repository;

import com.nelly.application.domain.BrandRank;
import com.nelly.application.domain.Brands;
import com.nelly.application.enums.BrandStatus;
import com.nelly.application.enums.DisplayType;
import com.nelly.application.enums.StyleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BrandRankRepository extends JpaRepository<BrandRank, Long> {
    Page<BrandRank> findAll(Pageable pageable);

    Page<BrandRank> findAllByBrandIn(List<Brands> brandList, Pageable pageable);
}
