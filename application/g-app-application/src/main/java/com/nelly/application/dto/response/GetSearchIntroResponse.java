package com.nelly.application.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class GetSearchIntroResponse {
    private List<String> currentKeywordList;
    private List<String> hotKeywordList;
    private List<SearchBrandResponse> hotBrandList;
}