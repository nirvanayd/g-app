package com.nelly.application.dto.response;

import com.nelly.application.dto.CommonListResponse;
import lombok.Data;

import java.util.List;

@Data
public class GetUserBrandsResponse extends CommonListResponse {
    private long totalPage;
    private long totalCount;
    private List<BrandFavoriteResponse> brandList;
}
