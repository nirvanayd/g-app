package com.nelly.application.repository;

import com.nelly.application.domain.BrandStyles;
import com.nelly.application.enums.StyleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandStylesRepository extends JpaRepository<BrandStyles, Long> {
    List<BrandStyles> findAllByBrandStyleIn(List<StyleType> styleTypeList);
}
