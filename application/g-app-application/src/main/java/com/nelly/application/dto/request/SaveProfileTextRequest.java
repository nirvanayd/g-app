package com.nelly.application.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class SaveProfileTextRequest {
    @NotNull
    @Size(max = 150, message = "프로필내용은 150자까지 입력 가능합니다.")
    private String profileText;
}
