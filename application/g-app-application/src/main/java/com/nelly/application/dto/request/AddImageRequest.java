package com.nelly.application.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class AddImageRequest {
    private String photoURL;
    private List<AddTagRequest> tagList;
}
