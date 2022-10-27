package com.nelly.application.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SaveBackgroundImageRequest {
    private String imageUrl;
}
