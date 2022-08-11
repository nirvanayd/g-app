package com.nelly.application.repository;

import com.nelly.application.domain.BrandAges;
import com.nelly.application.domain.BrandStyles;
import com.nelly.application.enums.AgeType;
import com.nelly.application.enums.StyleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandAgesRepository extends JpaRepository<BrandAges, Long> {
    List<BrandAges> findAllByAgeTypeIn(List<AgeType> ageTypeList);
}
