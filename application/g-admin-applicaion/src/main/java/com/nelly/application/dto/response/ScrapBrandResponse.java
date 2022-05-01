package com.nelly.application.dto.response;

import com.nelly.application.enums.IsUsedType;
import com.nelly.application.enums.ScraperStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ScrapBrandResponse {
    private Long id;
    private String name;
    private String moduleName;
    private IsUsedType isUsed;
    private String sampleUrl;
    private ScraperStatus status;
    private LocalDateTime lastSimulationDatetime;
    private List<ScrapBrandDetailResponse> scraperBrandDetails;


    private String statusCode;
    private String statusDesc;
    private Boolean isUsedCode;
    private String isUsedDesc;

    public void setIsUsed(IsUsedType isUsed) {
        this.isUsed = isUsed;
        this.isUsedCode = isUsed.getCode();
        this.statusDesc = isUsed.getDesc();
    }

    public void setStatus(ScraperStatus status) {
        this.status = status;
        this.statusCode = status.getCode();
        this.statusDesc = status.getDesc();
    }

}
