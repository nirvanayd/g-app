package com.nelly.application.dto.request;

import com.nelly.application.enums.YesOrNoType;
import com.nelly.application.utils.annotation.EnumValidator;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SaveMarkRequest {
    @NotNull
    private Long contentId;

    @NotNull
    @EnumValidator(enumClass = YesOrNoType.class, message = "좋아요 값이 올바르지 않습니다.", enumMethod = "hasCode")
    private String markYn;
}
