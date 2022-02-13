package com.nelly.application.service.brand;

import com.nelly.application.domain.Brands;
import com.nelly.application.dto.request.AddBrandRequest;
import com.nelly.application.dto.request.GetBrandListRequest;
import com.nelly.application.dto.response.BrandImageUploadResponse;
import com.nelly.application.enums.BrandStatus;
import com.nelly.application.enums.DisplayType;
import com.nelly.application.service.BrandDomainService;
import com.nelly.application.util.S3Uploader;
import dto.EnumIntegerCodeValue;
import dto.EnumStringCodeValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrandService {

    private final S3Uploader s3Uploader;
    private final BrandDomainService brandDomainService;

    @Value("${cloud.aws.s3.brand-dir}")
    public String brandDirectory;

    @Value("${cloud.aws.s3.cloudfront-url}")
    public String cloudFrontUrl;

    @Value("${cloud.aws.s3.default-image-url}")
    public String defaultImageUrl;

    public void addBrand(AddBrandRequest requestDto) {
        // set initial value
        Brands brands = brandDomainService.createBrands(requestDto.getName(), requestDto.getLogoImageUrl(), requestDto.getDescription(),
                (requestDto.getStatus() == null) ? BrandStatus.NORMAL : BrandStatus.getBrandStatus(requestDto.getStatus()),
                (requestDto.getIsDisplay() == null) ? DisplayType.NONE_DISPLAY : DisplayType.getDisplayType(requestDto.getIsDisplay()),
                requestDto.getHomepage(),
                requestDto.getIntroduceImageUrl());
        brandDomainService.saveBrandStyles(brands, requestDto.getBrandStyles());
        brandDomainService.saveBrandAges(brands, requestDto.getBrandAges());
        brandDomainService.saveBrandPlaces(brands, requestDto.getBrandPlaces());
    }

    public Page<Brands> getBrandList(GetBrandListRequest requestDto) {
        BrandStatus status = (requestDto.getStatusCode() == null) ? null : BrandStatus.getBrandStatus(requestDto.getStatusCode());
        DisplayType display = (requestDto.getDisplayCode() == null) ? null : DisplayType.getDisplayType(requestDto.getDisplayCode());
        return brandDomainService.selectBrandList(requestDto.getPage(), requestDto.getSize(), requestDto.getName(),
                status, display);
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


    public BrandImageUploadResponse saveImages(MultipartFile logoImage, MultipartFile introduceImage) throws IOException {
        String updateLogoImageUrl = null;
        String updateIntroduceImageUrl = null;
        if (logoImage != null) {
            updateLogoImageUrl = s3Uploader.upload(logoImage, getS3BrandPath());
        }

        if (introduceImage != null) {
            updateIntroduceImageUrl = s3Uploader.upload(introduceImage, getS3BrandPath());
        }

        return BrandImageUploadResponse.builder()
                .logoImageUrl(updateLogoImageUrl)
                .introduceImageUrl(updateIntroduceImageUrl)
                .build();
    }

    public BrandImageUploadResponse updateImages(long id, MultipartFile logoImage, MultipartFile introduceImage) throws IOException {
        Brands brands = this.getBrand(id);

        String logoImageUrl = brands.getLogoImageUrl();
        String introduceImageUrl = brands.getIntroduceImageUrl();

        BrandImageUploadResponse brandImageUploadResponse = this.saveImages(logoImage, introduceImage);

        String logoKey = logoImageUrl.replaceAll(cloudFrontUrl, "");
        String introduceKey = introduceImageUrl.replaceAll(cloudFrontUrl, "");

        s3Uploader.removeObject(logoKey);
        s3Uploader.removeObject(introduceKey);

        return brandImageUploadResponse;
    }

    public String getS3BrandPath() {
        return brandDirectory;
    }

    @Transactional
    public void updateBrand(long brandId, AddBrandRequest requestDto) {

        Brands brands = brandDomainService.updateBrand(brandId, requestDto.getName(),
                requestDto.getLogoImageUrl(), requestDto.getDescription(),
                (requestDto.getStatus() == null) ? BrandStatus.NORMAL : BrandStatus.getBrandStatus(requestDto.getStatus()),
                (requestDto.getIsDisplay() == null) ? DisplayType.NONE_DISPLAY : DisplayType.getDisplayType(requestDto.getIsDisplay()),
                requestDto.getHomepage(),
                requestDto.getIntroduceImageUrl());

        brandDomainService.saveBrandStyles(brands, requestDto.getBrandStyles());
        brandDomainService.saveBrandAges(brands, requestDto.getBrandAges());
        brandDomainService.saveBrandPlaces(brands, requestDto.getBrandPlaces());
    }
}
