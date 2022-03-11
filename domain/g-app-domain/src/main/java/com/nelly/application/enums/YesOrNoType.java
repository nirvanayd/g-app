package com.nelly.application.enums;

import com.nelly.application.enums.enumInterface.CommonEnums;
import com.nelly.application.enums.enumInterface.CommonStringCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum YesOrNoType implements CommonStringCode {
    YES("Y", "예"),
    NO("N", "아니오"),
    EMPTY(null, "");

    private final String code;
    private final String desc;

    public static boolean hasCode(String code) {
        return Arrays.stream(YesOrNoType.values()).anyMatch(c -> c.code.equals(code));
    }

    public static YesOrNoType getYesOrNoType(String code) {
        return Arrays.stream(YesOrNoType.values()).filter(c -> c.code.equals(code)).findFirst().orElse(EMPTY);
    }

    public static boolean hasCodeValueExist(String code) {
        if (code == null || "".equals(code)) return true;
        return hasCode(code);
    }

}
