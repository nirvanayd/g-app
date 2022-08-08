package com.nelly.application.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class GetRankResponse {
    private long totalPage;
    private long totalCount;
    private List<BrandRankResponse> brandList;
}
