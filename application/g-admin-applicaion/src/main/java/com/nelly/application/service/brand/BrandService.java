package com.nelly.application.service.brand;

import com.nelly.application.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrandService {

    private final S3Uploader s3Uploader;

    public void createBrand() {

    }

    public String uploadLogoImage(MultipartFile multipartFile) throws IOException {
        return s3Uploader.upload(multipartFile, "static/logo");
    }

    public String uploadIntroduceImage(MultipartFile multipartFile) throws IOException {
        return s3Uploader.upload(multipartFile, "static/introduce");
    }
}
