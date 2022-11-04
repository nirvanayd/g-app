package com.nelly.application.repository;

import com.nelly.application.domain.BrandAges;
import com.nelly.application.domain.BrandPlaces;
import com.nelly.application.domain.BrandStyles;
import com.nelly.application.domain.Brands;
import com.nelly.application.enums.BrandStatus;
import com.nelly.application.enums.DisplayType;
import com.nelly.application.enums.StyleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BrandsRepository extends JpaRepository<Brands, Long> {
    // findAll(Pageable)
    Page<Brands> findAllByNameContaining(String name, Pageable pageable);
    Page<Brands> findAllByNameKrContaining(String keyword, Pageable pageable);
    Page<Brands> findAllByIsDisplay(DisplayType displayType, Pageable pageable);
    Page<Brands> findAllByStatus(BrandStatus status, Pageable pageable);

    Page<Brands> findAllByNameContainingAndIsDisplay(String name, DisplayType displayType, Pageable pageable);
    Page<Brands> findAllByNameContainingAndStatus(String name, BrandStatus status, Pageable pageable);
    Page<Brands> findAllByStatusAndIsDisplay(BrandStatus status, DisplayType displayType, Pageable pageable);

    Page<Brands> findAllByNameContainingAndStatusAndIsDisplay(String name, BrandStatus status, DisplayType isDisplay, Pageable pageable);

    List<Brands> findAllByBrandStylesInAndBrandPlacesInAndBrandAgesIn(List<BrandStyles> brandStyleList,
                                                                      List<BrandPlaces> brandPlaceList,
                                                                      List<BrandAges> brandAgeList);

    List<Brands> findAllByBrandStylesInAndBrandPlacesIn(List<BrandStyles> brandStyleList,
                                                                      List<BrandPlaces> brandPlaceList);

    List<Brands> findAllByBrandPlacesInAndBrandAgesIn(List<BrandPlaces> brandPlaceList,
                                                                      List<BrandAges> brandAgeList);

    List<Brands> findAllByBrandStylesInAndBrandAgesIn(List<BrandStyles> brandStyleList,
                                                           List<BrandAges> brandAgeList);

    List<Brands> findAllByBrandStylesIn(List<BrandStyles> brandStyleList);
    List<Brands> findAllByBrandAgesIn(List<BrandAges> brandAgeList);
    List<Brands> findAllByBrandPlacesIn(List<BrandPlaces> brandPlaceList);

    @Transactional
    @Modifying
    @Query("UPDATE Brands b SET b.favoriteCount = b.favoriteCount + :value WHERE b.id = :brandId")
    void updateBrandFavoriteCount(@Param("brandId") Long contentId, @Param("value") int value);

    // app search
    // default : page, size, status
    // filter : name, style, place, age

    List<Brands> findAllByIdIn(List<Brands> brandStyleList);

    // app brand list
    List<Brands> findAllByStatusInAndIsDisplayOrderByName(List<BrandStatus> statusList, DisplayType displayType);
}
