package com.nelly.application.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class RemoveCurrentKeywordRequest {
    @NotEmpty
    private String keyword;
}
