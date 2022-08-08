package com.nelly.application.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nelly.application.enums.BrandStatus;
import com.nelly.application.enums.DisplayType;
import lombok.Data;

import java.util.List;

@Data
public class BrandRankResponse {
    private long id;
    private String name;
    private String description;
    private String logoImageUrl;
    @JsonIgnore
    private BrandStatus status;
    @JsonIgnore
    private DisplayType isDisplay;
    private String statusCode;
    private String statusDesc;
    private Integer displayCode;
    private String displayDesc;
    private String homepage;
    private String introduceImageUrl;
    private Integer ranking;
    private Integer favoriteCount;

    private List<BrandStyleResponse> brandStyles;
    private List<BrandAgeResponse> brandAges;
    private List<BrandPlaceResponse> brandPlaces;

    private Boolean isFavorite;

    public void setStatus(BrandStatus status) {
        this.status = status;
        this.statusCode = status.getCode();
        this.statusDesc = status.getDesc();
    }

    public void setIsDisplay(DisplayType isDisplay) {
        this.isDisplay = isDisplay;
        this.displayCode = isDisplay.getCode();
        this.displayDesc = isDisplay.getDesc();
    }

    public String getLogoImageUrl() {
        return (logoImageUrl == null) ? "https://d3b7cshusafhca.cloudfront.net/local/default/logo-default.png"
                : logoImageUrl;
    }

    public boolean getIsFavorite() {
        if (isFavorite == null) return false;
        return isFavorite;
    }


}
