package com.nelly.application.enums;

import com.nelly.application.enums.enumInterface.CommonEnums;
import com.nelly.application.enums.enumInterface.CommonIntegerCode;
import dto.EnumIntegerCodeValue;
import dto.EnumStringCodeValue;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public enum DisplayType implements CommonIntegerCode {
    DISPLAY(1, "노출"),
    NONE_DISPLAY(0, "미노출"),
    EMPTY(null, "");

    private final Integer code;
    private final String desc;

    public static boolean hasCode(Integer code) {
        return Arrays.stream(DisplayType.values()).anyMatch(c -> c.code.equals(code));
    }

    public static DisplayType getDisplayType(Integer code) {
        return Arrays.stream(DisplayType.values()).filter(c -> c.code.equals(code)).findFirst().orElse(EMPTY);
    }

    public static List<EnumIntegerCodeValue> getDisplayTypeList() {
        return Arrays.stream(DisplayType.values()).filter(c -> c.code != null).map(EnumIntegerCodeValue::new).collect(Collectors.toList());
    }
}
