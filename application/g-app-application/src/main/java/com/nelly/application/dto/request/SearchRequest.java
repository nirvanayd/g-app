package com.nelly.application.dto.request;

import lombok.Data;

@Data
public class SearchRequest extends PageRequest {
    private String keyword;
}
