package com.nelly.application.dto.response;

import lombok.Data;

@Data
public class BrandMainResponse {
    GetRankResponse brandRank;
    GetUserBrandsResponse getUserBrandsResponse;
}
