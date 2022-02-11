package com.nelly.application.controller;

import com.nelly.application.config.AwsProperties;
import com.nelly.application.config.EnvProperties;
import com.nelly.application.domain.Brands;
import com.nelly.application.dto.Response;
import com.nelly.application.dto.request.AddBrandRequest;
import com.nelly.application.dto.request.GetBrandListRequest;
import com.nelly.application.dto.response.*;
import com.nelly.application.service.brand.BrandService;
import com.nelly.application.util.S3Uploader;
import dto.EnumIntegerCodeValue;
import dto.EnumStringCodeValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BrandController {

    private final Response response;
    private final ModelMapper modelMapper;
    private final S3Uploader s3Uploader;
    private final BrandService brandService;
    private final EnvProperties envProperties;

    @PostMapping("/brands")
    public ResponseEntity<?> addBrand(@RequestBody @Valid AddBrandRequest requestDto) {
        brandService.addBrand(requestDto);
        return response.success();
    }

    @PostMapping("/brands/logo-image")
    public ResponseEntity<?> uploadLogoImage(@RequestParam("logoImage") MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) throw new RuntimeException("파일이 첨부되지 않았습니다.");
        String imageUrl = brandService.uploadLogoImage(multipartFile);
        FileUploadResponse fileUploadResponse = FileUploadResponse.builder().url(imageUrl).build();
        return response.success(fileUploadResponse);
    }

    @PostMapping("/brands/introduce-image")
    public ResponseEntity<?> uploadIntroduceImage(@RequestParam("introduceImage") MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) throw new RuntimeException("파일이 첨부되지 않았습니다.");
        String imageUrl = brandService.uploadIntroduceImage(multipartFile);
        FileUploadResponse fileUploadResponse = FileUploadResponse.builder().url(imageUrl).build();
        return response.success(fileUploadResponse);
    }

    @GetMapping("/brands")
    public ResponseEntity<?> getBrandList(@Valid GetBrandListRequest requestDto) {
        System.out.println(requestDto);
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
    public ResponseEntity<?> saveBrandImages(@RequestParam("logo-image") MultipartFile logoImage,
                                             @RequestParam("introduce-image") MultipartFile introduceImage) throws IOException {

        String logoImageUrl = s3Uploader.upload(logoImage, "brand");
        String introduceImageUrl = s3Uploader.upload(introduceImage, "brand");

        BrandImageUploadResponse brandImageUploadResponse = BrandImageUploadResponse.builder()
                .logoImageUrl(logoImageUrl)
                .introduceImageUrl(introduceImageUrl)
                .build();

        return response.success(brandImageUploadResponse);
    }
}
