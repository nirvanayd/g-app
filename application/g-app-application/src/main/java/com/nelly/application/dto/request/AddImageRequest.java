package com.nelly.application.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class AddImageRequest {
    private String imageUrl;
    private List<AddTagRequest> tagList;
}
