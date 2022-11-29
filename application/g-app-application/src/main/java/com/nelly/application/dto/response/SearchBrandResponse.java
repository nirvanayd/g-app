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
    // flutter model 적용파라미터
    private String photoURL;

    public String getPhotoURL() {
        return (photoURL == null) ? "https://d3b7cshusafhca.cloudfront.net/local/default/logo-default.png"
                : photoURL;
    }
}
