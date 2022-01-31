package com.nelly.application.enums;

import com.nelly.application.enums.enumInterface.CommonEnums;
import com.nelly.application.enums.enumInterface.CommonStringCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum AgeType implements CommonStringCode {
    EARLY_20("20E", "20대 초반"),
    MID_20("20M", "20대 중반"),
    LATE_20("20L", "20대 후반"),
    EARLY_30("30E", "30대 후반"),
    MID_30("30M", "30대 후반"),
    LATE_30("30L", "30대 후반"),
    EARLY_40("40E", "40대 후반"),
    MID_40("40M", "40대 후반"),
    LATE_40("40L", "40대 후반"),
    ETC("etc", "기타"),
    EMPTY(null, "");

    private final String code;
    private final String desc;

    public static boolean hasCode(String code) {
        return Arrays.stream(AgeType.values()).anyMatch(c -> c.code.equals(code));
    }

    public static AgeType getAgeType(String code) {
        return Arrays.stream(AgeType.values()).filter(c -> c.code.equals(code)).findFirst().orElse(EMPTY);
    }
}
