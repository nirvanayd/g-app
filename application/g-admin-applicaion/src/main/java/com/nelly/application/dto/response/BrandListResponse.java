package com.nelly.application.dto.response;

import com.nelly.application.dto.response.common.ListResponse;
import lombok.Data;

import java.util.List;

@Data
public class BrandListResponse extends ListResponse {
    List<BrandResponse> list;
}
