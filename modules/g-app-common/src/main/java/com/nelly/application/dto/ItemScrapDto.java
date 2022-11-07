package com.nelly.application.dto;

import lombok.Data;

import java.util.List;

@Data
public class ItemScrapDto {
    private List<String> imageList;
    private String name;
    private String price;
    private String brandName;
}
