package com.nelly.application.service;

import com.nelly.application.domain.*;
import com.nelly.application.enums.*;
import com.nelly.application.exception.SystemException;
import com.nelly.application.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    private final BrandRankRepository brandRankRepository;
    private final UserBrandsRepository userBrandsRepository;

    public Brands createBrands(String name, String logoImageUrl, String description, BrandStatus status,
                               DisplayType isDisplay, String homepage, String introduceImageUrl) {
        Brands brands = Brands.builder()
                .name(name)
                .logoImageUrl(logoImageUrl)
                .description(description)
                .status(status)
                .isDisplay(isDisplay)
                .homepage(homepage)
                .introduceImageUrl(introduceImageUrl)
                .favoriteCount(0)
                .build();
        return brandsRepository.save(brands);
    }

    public void saveBrandStyles(Brands brands, List<String> list) {
        Brands refBrands = brandsRepository.getById(brands.getId());
        for (String code: list) {
            BrandStyles brandStyles = BrandStyles.builder()
                    .brandStyle(StyleType.getStyleType(code))
                    .brand(refBrands)
                    .build();
            brandStylesRepository.save(brandStyles);
        }
    }

    public void saveBrandPlaces(Brands brands, List<String> list) {
        Brands refBrands = brandsRepository.getById(brands.getId());
        for (String code: list) {
            BrandPlaces brandPlaces = BrandPlaces.builder()
                    .placeType(PlaceType.getPlaceType(code))
                    .brand(refBrands)
                    .build();
            brandPlacesRepository.save(brandPlaces);
        }
    }

    public void saveBrandAges(Brands brands, List<String> list) {
        Brands refBrands = brandsRepository.getById(brands.getId());
        for (String code: list) {
            BrandAges brandAges = BrandAges.builder()
                    .ageType(AgeType.getAgeType(code))
                    .brand(refBrands)
                    .build();
            brandAgesRepository.save(brandAges);
        }
    }

    public Page<Brands> selectBrandList(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        return brandsRepository.findAll(pageRequest);
    }

    public Page<Brands> selectBrandList(Integer page, Integer size, String name, BrandStatus status, DisplayType display) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        if (name == null && status == null && display == null) return selectBrandList(page, size);
        if (name == null) return selectBrandList(page, size, status, display);
        if (status == null && display == null) return brandsRepository.findAllByNameContaining(name, pageRequest);
        if (status == null) return brandsRepository.findAllByNameContainingAndIsDisplay(name, display, pageRequest);
        if (display == null) return brandsRepository.findAllByNameContainingAndStatus(name, status, pageRequest);
        return brandsRepository.findAllByNameContainingAndStatusAndIsDisplay(name, status, display, pageRequest);
    }

    public Page<Brands> selectBrandList(Integer page, Integer size, BrandStatus status, DisplayType display) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        if (status == null) return brandsRepository.findAllByIsDisplay(display, pageRequest);
        if (display == null) return brandsRepository.findAllByStatus(status, pageRequest);
        return brandsRepository.findAllByStatusAndIsDisplay(status, display, pageRequest);
    }

    public Brands selectBrand(long brandId) {
        return brandsRepository.findById(brandId).orElseThrow(() -> new RuntimeException("브랜드를 조회할 수 없습니다."));
    }

    public Brands updateBrand(long brandId, String name, String logoImageUrl, String description, BrandStatus status,
                              DisplayType isDisplay, String homepage, String introduceImageUrl) {
        Brands brands = brandsRepository.findById(brandId).orElseThrow(() -> new RuntimeException("브랜드를 조회할 수 없습니다."));

        brands.setName(name);
        brands.setLogoImageUrl(logoImageUrl);
        brands.setDescription(description);
        brands.setStatus(status);
        brands.setIsDisplay(isDisplay);
        brands.setHomepage(homepage);
        brands.setIntroduceImageUrl(introduceImageUrl);

        return brandsRepository.save(brands);
    }

    public void updateBrandStyles(Brands brands, List<String> list) {
        Brands refBrands = brandsRepository.getById(brands.getId());
        for (String code: list) {
            BrandStyles brandStyles = BrandStyles.builder()
                    .brandStyle(StyleType.getStyleType(code))
                    .brand(refBrands)
                    .build();
            brandStylesRepository.save(brandStyles);
        }
    }

    // app brand search
    public Page<BrandRank> selectAppBrandRankList(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("ranking").ascending());
        return brandRankRepository.findAll(pageRequest);
    }

    public List<UserBrands> selectAppUserBrandList(Long userId, List<Long> brandIdList) {
        return userBrandsRepository.findAllByUserIdAndBrandIdIn(userId, brandIdList);
    }

    public Optional<UserBrands> selectUserBrand(Long brandId, Long userId) {
        return  userBrandsRepository.findByBrandIdAndUserId(brandId, userId);
    }

    public Page<UserBrands> selectUserBrandList(Long userId, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("brandName").ascending());
        return userBrandsRepository.findAllByUserId(userId, pageRequest);
    }


    public void createUserBrand(Brands brand, Long userId) {
        UserBrands userBrands = UserBrands.builder().userId(userId).brand(brand).build();
        userBrandsRepository.save(userBrands);
    }

    public void deleteUserBrand(UserBrands userBrand) {
        userBrandsRepository.delete(userBrand);
    }

    public void updateBrandFavoriteCount(Long brandId, int value) {
        brandsRepository.updateBrandFavoriteCount(brandId, value);
    }
}
