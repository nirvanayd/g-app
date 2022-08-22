package com.nelly.application.dto.request;

import lombok.Data;

@Data
public class AddTagRequest {
    private Integer id;
    private double x;
    private double y;
    private String tag;
}
