package com.nelly.application.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SearchRequest extends PageRequest {
    @NotEmpty(message = "검색어를 입력해주세요.")
    private String keyword;
}
