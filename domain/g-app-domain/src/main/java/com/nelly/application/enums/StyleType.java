package com.nelly.application.enums;

import com.nelly.application.enums.enumInterface.CommonStringCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum StyleType implements CommonStringCode {
    CASUAL("casual", "캐주얼", true),
    SPORTY("sporty", "스포티", true),
    PROFESSIONAL("professional", "프로페셔널", true),
    LUXURY("luxury", "럭셔리", true),
    SIMPLE_BASIC("simple", "실플베이직", true),
    UNIQUE("unique", "유니크", true),
    EMPTY(null, "", false);

    private final String code;
    private final String desc;
    private final boolean used;

    public static boolean hasCode(String code) {
        return Arrays.stream(StyleType.values()).anyMatch(c -> c.code.equals(code));
    }

    public static StyleType getStyleType(String code) {
        return Arrays.stream(StyleType.values()).filter(c -> c.code.equals(code)).findFirst().orElse(EMPTY);
    }
}
