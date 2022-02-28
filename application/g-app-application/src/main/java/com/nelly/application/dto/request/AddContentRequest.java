package com.nelly.application.dto.request;

import com.nelly.application.dto.BrandTagDto;
import com.nelly.application.dto.ItemTagDto;
import com.nelly.application.dto.UserTagDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@Data
public class AddContentRequest {
    @NotNull
    private String contentText;
    private ArrayList<String> imageUrlList;
    private ArrayList<UserTagDto> userHashTags;
    private ArrayList<BrandTagDto> brandHashTags;
    private ArrayList<ItemTagDto> itemHashTags;
}
