package com.nelly.application.controller;

import com.nelly.application.config.AwsProperties;
import com.nelly.application.config.EnvProperties;
import com.nelly.application.domain.Brands;
import com.nelly.application.domain.ScraperBrands;
import com.nelly.application.dto.Response;
import com.nelly.application.dto.request.AddBrandRequest;
import com.nelly.application.dto.request.GetBrandListRequest;
import com.nelly.application.dto.response.*;
import com.nelly.application.service.brand.BrandService;
import com.nelly.application.service.scraper.ScraperService;
import com.nelly.application.util.S3Uploader;
import dto.EnumIntegerCodeValue;
import dto.EnumStringCodeValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BrandController {

    private final Response response;
    private final ModelMapper modelMapper;
    private final BrandService brandService;
    private final ScraperService scraperService;

    @PostMapping("/brands")
    public ResponseEntity<?> addBrand(@RequestBody @Valid AddBrandRequest requestDto) {
        brandService.addBrand(requestDto);
        return response.success();
    }

    @PutMapping("/brands/{brandId}")
    public ResponseEntity<?> updateBrand(@PathVariable long brandId,
                                         @RequestBody @Valid AddBrandRequest requestDto) {

        brandService.updateBrand(brandId, requestDto);
        return response.success("test");
    }

    @GetMapping("/brands")
    public ResponseEntity<?> getBrandList(@Valid GetBrandListRequest requestDto) {
        Page<Brands> brandsPage = brandService.getBrandList(requestDto);
        long totalCount = brandsPage.getTotalElements();
        long totalPage = brandsPage.getTotalPages();
        List<Brands> brandList = brandsPage.getContent();
        List<BrandResponse> list = brandList.stream().
                map(u -> modelMapper.map(u, BrandResponse.class)).collect(Collectors.toList());

        // response make
        BrandListResponse brandListResponse = new BrandListResponse();
        brandListResponse.setTotalCount(totalCount);
        brandListResponse.setTotalPage(totalPage);
        brandListResponse.setList(list);
        return response.success(brandListResponse);
    }

    @GetMapping("/brands/{brandId}")
    public ResponseEntity<?> getBrand(@PathVariable long brandId) {
        Brands brands = brandService.getBrand(brandId);

        BrandResponse brandResponse = new BrandResponse();
        modelMapper.map(brands, brandResponse);
        return response.success(brandResponse);
    }

    @GetMapping("/brands/status-list")
    public ResponseEntity<?> getBrandStatusList() {
        List<EnumStringCodeValue> list = brandService.getBrandStatusList();
        return response.success(list);
    }

    @GetMapping("/brands/display-type-list")
    public ResponseEntity<?> getDisplayTypeList() {
        List<EnumIntegerCodeValue> list = brandService.getDisplayTypeList();
        return response.success(list);
    }

    @PostMapping("/brands/save-images")
    public ResponseEntity<?> saveBrandImages(@Nullable @RequestParam("logo-image") MultipartFile logoImage,
                                             @Nullable @RequestParam("introduce-image") MultipartFile introduceImage) throws IOException {

        BrandImageUploadResponse brandImageUploadResponse = brandService.saveImages(logoImage, introduceImage);
        return response.success(brandImageUploadResponse);
    }


    @PostMapping("/brands/update-images")
    public ResponseEntity<?> saveBrandImages(@RequestParam("id") long id,
                                             @Nullable @RequestParam("logo-image") MultipartFile logoImage,
                                             @Nullable @RequestParam("introduce-image") MultipartFile introduceImage) throws IOException {

        BrandImageUploadResponse brandImageUploadResponse = brandService.updateImages(id, logoImage, introduceImage);
        return response.success(brandImageUploadResponse);
    }

    @GetMapping("/brands/scrap-all")
    public ResponseEntity<?> scrapAll() {
        scraperService.scrapAll();
        return response.success();
    }

    @PostMapping("/brands/scrap")
    public ResponseEntity<?> addScrap() {
        // 스크래핑 테스트에 추가할 브랜드 추가
        return response.success();
    }

    @GetMapping("/brands/scrap")
    public ResponseEntity<?> getScrapBrands() {
        // 페이징 처리하지 않음.
        List<ScraperBrands> scrapBrands = scraperService.getScraperBrands();

        List<ScrapBrandResponse> list = scrapBrands.stream().
                map(u -> modelMapper.map(u, ScrapBrandResponse.class)).collect(Collectors.toList());

        ScrapBrandListResponse scrapBrandListResponse = new ScrapBrandListResponse();
        scrapBrandListResponse.setTotalCount(list.size());
        scrapBrandListResponse.setTotalPage(0);
        scrapBrandListResponse.setList(list);
        return response.success(scrapBrandListResponse);
    }
}
