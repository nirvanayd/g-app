package com.nelly.application.repository;

import com.nelly.application.domain.Brands;
import com.nelly.application.domain.UserBrands;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserBrandsRepository extends JpaRepository<UserBrands, Long> {
    List<UserBrands> findAllByUserIdAndBrandIdIn(Long userId, List<Long> brandIdList);
    Optional<UserBrands> findByUserIdAndBrand(Long userId, Brands brand);
    Optional<UserBrands> findByBrandIdAndUserId(Long brandId, Long userId);
    Page<UserBrands> findAllByUserId(Long userId, Pageable pageable);
}
