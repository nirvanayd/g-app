package com.nelly.application.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nelly.application.dto.CommonListResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class SearchBrandResponse {
    private long id;
    private String name;
    @JsonIgnore
    private String logoImageUrl;
    // flutter model 적용파라미터
    private String photoURL;

    public void setLogoImageUrl(String logoImageUrl) {
        this.logoImageUrl = logoImageUrl;
        this.photoURL = logoImageUrl;
    }

    public String getPhotoURL() {
        return (photoURL == null) ? "https://d3b7cshusafhca.cloudfront.net/local/default/logo-default.png"
                : photoURL;
    }

    public String getLogoImageUrl() {
        return (logoImageUrl == null) ? "https://d3b7cshusafhca.cloudfront.net/local/default/logo-default.png"
                : logoImageUrl;
    }
}
