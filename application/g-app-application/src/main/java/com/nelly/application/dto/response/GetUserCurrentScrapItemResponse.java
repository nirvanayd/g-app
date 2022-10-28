package com.nelly.application.dto.response;

import com.nelly.application.dto.request.PageRequest;
import lombok.Data;

import java.util.List;

@Data
public class GetUserCurrentScrapItemResponse {
    long totalCount;
    List<ScrapItemResponse> list;
}
