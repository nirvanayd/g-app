package com.nelly.application.service;

import com.nelly.application.domain.*;
import com.nelly.application.enums.AgeType;
import com.nelly.application.enums.BrandStatus;
import com.nelly.application.enums.PlaceType;
import com.nelly.application.enums.StyleType;
import com.nelly.application.repository.BrandAgesRepository;
import com.nelly.application.repository.BrandPlacesRepository;
import com.nelly.application.repository.BrandStylesRepository;
import com.nelly.application.repository.BrandsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrandDomainService {

    private final BrandsRepository brandsRepository;
    private final BrandStylesRepository brandStylesRepository;
    private final BrandPlacesRepository brandPlacesRepository;
    private final BrandAgesRepository brandAgesRepository;

    public Brands createBrands(String name, String logoImageUrl, String description, BrandStatus status, Integer isDisplay,
                                 String homepage, String introduceImageUrl) {
        Brands brands = Brands.builder()
                .name(name)
                .logoImageUrl(logoImageUrl)
                .description(description)
                .status(status)
                .isDisplay(isDisplay)
                .homepage(homepage)
                .introduceImageUrl(introduceImageUrl)
                .build();
        return brandsRepository.save(brands);
    }

    public void createBrandStyles(Brands brands, List<String> list) {
        Brands refBrands = brandsRepository.getById(brands.getId());
        for (String code: list) {
            BrandStyles brandStyles = BrandStyles.builder()
                    .brandStyle(StyleType.getStyleType(code))
                    .brand(refBrands)
                    .build();
            brandStylesRepository.save(brandStyles);
        }
    }

    public void createBrandPlaces(Brands brands, List<String> list) {
        Brands refBrands = brandsRepository.getById(brands.getId());
        for (String code: list) {
            BrandPlaces brandPlaces = BrandPlaces.builder()
                    .placeType(PlaceType.getPlaceType(code))
                    .brand(refBrands)
                    .build();
            brandPlacesRepository.save(brandPlaces);
        }
    }

    public void createBrandAges(Brands brands, List<String> list) {
        Brands refBrands = brandsRepository.getById(brands.getId());
        for (String code: list) {
            BrandAges brandAges = BrandAges.builder()
                    .ageType(AgeType.getAgeType(code))
                    .brand(refBrands)
                    .build();
            brandAgesRepository.save(brandAges);
        }
    }

    public Page<Brands> selectBrandsList(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return brandsRepository.findAll(pageRequest);
    }
}
