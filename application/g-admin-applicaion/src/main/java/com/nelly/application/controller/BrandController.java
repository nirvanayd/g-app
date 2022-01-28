package com.nelly.application.controller;

import com.nelly.application.config.AwsProperties;
import com.nelly.application.dto.Response;
import com.nelly.application.dto.request.CreateBrandRequest;
import com.nelly.application.dto.response.FileUploadResponse;
import com.nelly.application.service.AppUserService;
import com.nelly.application.service.brand.BrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BrandController {

    private final Response response;
    private final ModelMapper modelMapper;
    private final AwsProperties awsProperties;
    private final BrandService brandService;

    @PostMapping("/brands")
    public ResponseEntity<?> createBrand(@RequestPart("") CreateBrandRequest requestDto) {
        System.out.println("########");
        System.out.println(awsProperties.getS3().getDirectory());
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
}
