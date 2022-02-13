package com.nelly.application.repository;

import com.nelly.application.domain.Brands;
import com.nelly.application.enums.BrandStatus;
import com.nelly.application.enums.DisplayType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandsRepository extends JpaRepository<Brands, Long> {
    // findAll(Pageable)
    Page<Brands> findAllByNameContaining(String name, Pageable pageable);
    Page<Brands> findAllByIsDisplay(DisplayType displayType, Pageable pageable);
    Page<Brands> findAllByStatus(BrandStatus status, Pageable pageable);

    Page<Brands> findAllByNameContainingAndIsDisplay(String name, DisplayType displayType, Pageable pageable);
    Page<Brands> findAllByNameContainingAndStatus(String name, BrandStatus status, Pageable pageable);
    Page<Brands> findAllByStatusAndIsDisplay(BrandStatus status, DisplayType displayType, Pageable pageable);

    Page<Brands> findAllByNameContainingAndStatusAndIsDisplay(String name, BrandStatus status, DisplayType isDisplay, Pageable pageable);
}
