package com.nelly.application.dto.request;

import com.nelly.application.enums.StyleType;
import com.nelly.application.utils.annotation.EnumListValidator;
import lombok.Data;

import java.util.List;

@Data
public class UpdateUserStyleRequest {
    @EnumListValidator(enumClass = StyleType.class, message = "스타일 유형이 올바르지 않습니다.", enumMethod = "hasCode")
    private List<String> userStyle;
}
