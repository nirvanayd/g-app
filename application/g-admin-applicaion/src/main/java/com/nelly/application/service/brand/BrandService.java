package com.nelly.application.service.brand;

import com.nelly.application.domain.Brands;
import com.nelly.application.dto.request.AddBrandRequest;
import com.nelly.application.dto.request.GetBrandListRequest;
import com.nelly.application.enums.BrandStatus;
import com.nelly.application.enums.DisplayType;
import com.nelly.application.service.BrandDomainService;
import com.nelly.application.util.S3Uploader;
import dto.EnumIntegerCodeValue;
import dto.EnumStringCodeValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrandService {

    private final S3Uploader s3Uploader;
    private final BrandDomainService brandDomainService;

    public void addBrand(AddBrandRequest requestDto) {
        // set initial value
        if (requestDto.getIsDisplay() == null) requestDto.setIsDisplay(DisplayType.DISPLAY);
        if (requestDto.getStatus() == null) requestDto.setStatus(BrandStatus.NORMAL);
        Brands brands = brandDomainService.createBrands(requestDto.getName(), requestDto.getLogoImageUrl(), requestDto.getDescription(),
                requestDto.getStatus(), requestDto.getIsDisplay(), requestDto.getHomepage(),
                requestDto.getIntroduceImageUrl());
        brandDomainService.createBrandStyles(brands, requestDto.getBrandStyle());
        brandDomainService.createBrandAges(brands, requestDto.getAgeType());
        brandDomainService.createBrandPlaces(brands, requestDto.getPlaceType());
    }

    public String uploadLogoImage(MultipartFile multipartFile) throws IOException {
        return s3Uploader.upload(multipartFile, "static/logo");
    }

    public String uploadIntroduceImage(MultipartFile multipartFile) throws IOException {
        return s3Uploader.upload(multipartFile, "static/introduce");
    }

    public Page<Brands> getBrandList(GetBrandListRequest requestDto) {
        return brandDomainService.selectBrandList(requestDto.getPage(), requestDto.getSize());
    }

    public Brands getBrand(long brandId) {
        return brandDomainService.selectBrand(brandId);
    }

    public List<EnumStringCodeValue> getBrandStatusList() {
        return BrandStatus.getBrandStatusList();
    }

    public List<EnumIntegerCodeValue> getDisplayTypeList() {
        return DisplayType.getDisplayTypeList();
    }
}
