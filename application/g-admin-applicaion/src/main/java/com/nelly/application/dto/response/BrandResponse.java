package com.nelly.application.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nelly.application.enums.BrandStatus;
import lombok.Data;

import java.util.List;

@Data
public class BrandResponse {
    private long id;
    private String name;
    private String description;
    private String logoImageUrl;
    @JsonIgnore
    private BrandStatus status;
    private String statusCode;
    private String statusDesc;
    private Integer isDisplay;
    private String homepage;
    private String introduceImageUrl;

    List<BrandStyleResponse> brandStyles;
    List<BrandAgeResponse> brandAges;
    List<BrandPlaceResponse> brandPlaces;

    public void setStatus(BrandStatus status) {
        this.status = status;
        this.statusCode = status.getCode();
        this.statusDesc = status.getDesc();
    }
}
