package com.nelly.application.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchLogResponse {
    private String keyword;
    private int type;
}