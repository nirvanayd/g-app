package com.nelly.application.dto.response;

import com.nelly.application.domain.ScraperBrands;
import lombok.Data;

@Data
public class ScrapBrandDetailResponse {
    private Long id;
    private boolean isHttps;
    private String host;
    private Integer port;
    private String itemPath;
}
