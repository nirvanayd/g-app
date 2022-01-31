package com.nelly.application.enums;

import com.nelly.application.enums.enumInterface.CommonEnums;
import com.nelly.application.enums.enumInterface.CommonStringCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum BrandStatus implements CommonStringCode {
    NORMAL("00", "정상"),
    BLOCK("10", "점검"),
    LEAVE("99", "미사용"),
    EMPTY(null, "");

    private final String code;
    private final String desc;

    public static boolean hasCode(String code) {
        return Arrays.stream(BrandStatus.values()).anyMatch(c -> c.code.equals(code));
    }

    public static BrandStatus getBrandStatus(String code) {
        return Arrays.stream(BrandStatus.values()).filter(c -> c.code.equals(code)).findFirst().orElse(EMPTY);
    }
}
