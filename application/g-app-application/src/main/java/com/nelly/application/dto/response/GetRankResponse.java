package com.nelly.application.dto.response;

import com.nelly.application.dto.CommonListResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetRankResponse extends CommonListResponse {
    private long totalPage;
    private long totalCount;
    private List<BrandRankResponse> brandList;
}
