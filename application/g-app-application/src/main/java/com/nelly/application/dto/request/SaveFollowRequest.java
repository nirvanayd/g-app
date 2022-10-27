package com.nelly.application.dto.request;

import com.nelly.application.enums.YesOrNoType;
import com.nelly.application.utils.annotation.EnumValidator;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SaveFollowRequest {
    @NotNull
    private String userId;

    @NotNull
    @EnumValidator(enumClass = YesOrNoType.class, message = "팔로우여부 값이 올바르지 않습니다.", enumMethod = "hasCode")
    private String followYn;
}
