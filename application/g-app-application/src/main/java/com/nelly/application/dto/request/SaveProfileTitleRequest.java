package com.nelly.application.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class SaveProfileTitleRequest {
    @NotNull
    @Size(max = 20, message = "프로필제목은 20자까지 입력 가능합니다.")
    private String profileTitle;
}
