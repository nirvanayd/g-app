package com.nelly.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TagResponse {
    private Integer id;
    private String tag;
    private Double x;
    private Double y;
    private Long brandId;
}
