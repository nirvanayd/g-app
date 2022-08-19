package com.nelly.application.dto.request;

import com.nelly.application.dto.BrandTagDto;
import com.nelly.application.dto.UserTagDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@Data
public class AddContentRequest {
    @NotNull
    private String contentText;
    private ArrayList<AddImageRequest> imageList;
    private ArrayList<UserTagDto> userHashTags;
    private ArrayList<BrandTagDto> brandHashTags;
}
