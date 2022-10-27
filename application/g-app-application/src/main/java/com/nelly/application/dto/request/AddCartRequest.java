package com.nelly.application.dto.request;

import lombok.Data;

@Data
public class AddCartRequest {
    private String url;
    private Long id;
}