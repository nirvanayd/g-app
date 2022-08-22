package com.nelly.application.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nelly.application.dto.CommonListResponse;
import com.nelly.application.enums.BrandStatus;
import com.nelly.application.enums.DisplayType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
public class BrandInitResponse {
    private long id;
    private String name;
    private String description;
    @JsonIgnore
    private String logoImageUrl;
    @JsonIgnore
    private Integer favoriteCount;
    @JsonIgnore
    private Boolean isFavorite;

    // flutter model 적용파라미터
    private String photoURL;
    @JsonIgnore
    private Integer likeCount;
    @JsonIgnore
    private Boolean liked;

    @JsonIgnore
    private BrandStatus status;
    @JsonIgnore
    private DisplayType isDisplay;
    @JsonIgnore
    private String statusCode;
    @JsonIgnore
    private String statusDesc;
    @JsonIgnore
    private Integer displayCode;
    @JsonIgnore
    private String displayDesc;
    @JsonIgnore
    private String homepage;
    @JsonIgnore
    private String introduceImageUrl;
    @JsonIgnore
    private List<BrandStyleResponse> brandStyles;
    @JsonIgnore
    private List<BrandAgeResponse> brandAges;
    @JsonIgnore
    private List<BrandPlaceResponse> brandPlaces;

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

    public void setLogoImageUrl(String logoImageUrl) {
        this.logoImageUrl = logoImageUrl;
        this.photoURL = logoImageUrl;
    }

    public void setFavoriteCount(Integer favoriteCount) {
        this.favoriteCount = favoriteCount;
        this.likeCount = favoriteCount;
    }

    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
        this.liked = isFavorite;
    }

    public String getPhotoURL() {
        return (photoURL == null) ? "https://d3b7cshusafhca.cloudfront.net/local/default/logo-default.png"
                : photoURL;
    }

    public String getLogoImageUrl() {
        return (logoImageUrl == null) ? "https://d3b7cshusafhca.cloudfront.net/local/default/logo-default.png"
                : logoImageUrl;
    }

    public boolean getIsFavorite() {
        if (isFavorite == null) return false;
        return isFavorite;
    }

    public boolean getLiked() {
        if (isFavorite == null) return false;
        return isFavorite;
    }
}
