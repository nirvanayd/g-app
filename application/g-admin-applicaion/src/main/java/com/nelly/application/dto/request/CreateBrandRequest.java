package com.nelly.application.dto.request;

import com.nelly.application.enums.BrandStatus;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateBrandRequest {
    private Long id;
    private String name;
    private String description;
    private BrandStatus status;
    private Integer isDisplay;
    private String homepage;
    private MultipartFile logoImageUrl;
    private MultipartFile introduceImage;
}
