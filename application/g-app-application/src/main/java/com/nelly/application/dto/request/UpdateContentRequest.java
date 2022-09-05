package com.nelly.application.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@Data
public class UpdateContentRequest {
    @NotNull
    private String text;
    private ArrayList<AddImageRequest> photoList;
}
