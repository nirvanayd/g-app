package com.nelly.application.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class GetUserCartResponse {
    long totalCount;
    List<ScrapItemResponse> list;
}
