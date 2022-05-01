package com.nelly.application.enums;

import com.nelly.application.enums.enumInterface.CommonBooleanCode;
import dto.EnumBooleanCodeValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public enum IsUsedType implements CommonBooleanCode {
    DISPLAY(true, "사용"),
    NONE_DISPLAY(false, "미사용"),
    EMPTY(null, "");

    private final Boolean code;
    private final String desc;

    public static boolean hasCode(boolean code) {
        return Arrays.stream(IsUsedType.values()).anyMatch(c -> c.code == code);
    }

    public static boolean hasCodeValueExist(Boolean code) {
        if (code == null) return true;
        return hasCode(code);
    }

    public static IsUsedType getIsUsedType(Boolean code) {
        return Arrays.stream(IsUsedType.values()).filter(c -> c.code == code).findFirst().orElse(EMPTY);
    }

    public static List<EnumBooleanCodeValue> getIsUsedTypeList() {
        return Arrays.stream(IsUsedType.values()).map(EnumBooleanCodeValue::new).collect(Collectors.toList());
    }
}
