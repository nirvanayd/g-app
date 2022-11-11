package com.nelly.application.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class GetSearchIntroResponse {
    private List<SearchLogResponse> currentKeywordList;
    private List<SearchLogResponse> hotKeywordList;
    private List<SearchLogResponse> hotBrandList;
}
