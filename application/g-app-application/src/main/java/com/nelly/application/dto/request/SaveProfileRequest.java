package com.nelly.application.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class SaveProfileRequest {
    @NotNull
    @Size(max = 500)
    private String profileTitle;

    @NotNull
    @Size(max = 500)
    private String profileText;
}
