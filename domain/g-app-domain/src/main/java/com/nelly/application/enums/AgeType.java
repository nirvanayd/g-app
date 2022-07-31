package com.nelly.application.enums;

import com.nelly.application.enums.enumInterface.CommonEnums;
import com.nelly.application.enums.enumInterface.CommonStringCode;
import dto.EnumStringCodeValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public enum AgeType implements CommonStringCode {
    AGE_20("20", "20대"),
    AGE_30("30", "30대 후반"),
    AGE_40("40", "40대 후반"),
    AGE_50("50", "40대 후반"),
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

    public static List<EnumStringCodeValue> getAgeList() {
        return Arrays.stream(AgeType.values()).filter(c -> c.code != null).map(EnumStringCodeValue::new).collect(Collectors.toList());
    }
}
