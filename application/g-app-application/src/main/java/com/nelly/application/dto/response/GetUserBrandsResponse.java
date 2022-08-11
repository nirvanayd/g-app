package com.nelly.application.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class GetUserBrandsResponse {
    private long totalPage;
    private long totalCount;
    private List<BrandFavoriteResponse> brandList;
}
