package com.nelly.application.dto.request;

import com.nelly.application.dto.BrandTagDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@Data
public class AddContentRequest {
    @NotNull
    private String text;
    private ArrayList<AddImageRequest> photoList;
}
