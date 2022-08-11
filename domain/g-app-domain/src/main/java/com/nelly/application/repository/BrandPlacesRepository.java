package com.nelly.application.repository;

import com.nelly.application.domain.BrandPlaces;
import com.nelly.application.domain.BrandStyles;
import com.nelly.application.enums.PlaceType;
import com.nelly.application.enums.StyleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandPlacesRepository extends JpaRepository<BrandPlaces, Long> {
    List<BrandPlaces> findAllByPlaceTypeIn(List<PlaceType> placeTypeList);
}
