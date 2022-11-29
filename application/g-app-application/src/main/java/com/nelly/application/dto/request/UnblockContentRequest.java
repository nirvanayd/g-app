package com.nelly.application.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UnblockContentRequest {
    @NotNull(message = "컨텐츠는 필수값입니다.")
    private Long contentId;

    @NotEmpty(message = "요청사유는 필수값입니다.")
    @NotNull(message = "요청사유는 필수값입니다.")
    private String text;
}