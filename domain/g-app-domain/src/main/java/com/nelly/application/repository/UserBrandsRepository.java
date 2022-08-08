package com.nelly.application.repository;

import com.nelly.application.domain.UserBrands;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserBrandsRepository extends JpaRepository<UserBrands, Long> {
    List<UserBrands> findAllByUserIdAndBrandIdIn(Long userId, List<Long> brandIdList);

    Optional<UserBrands> findByBrandIdAndUserId(Long brandId, Long userId);
}
