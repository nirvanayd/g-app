package com.nelly.application.enums;

import com.nelly.application.enums.enumInterface.CommonStringCode;
import dto.EnumStringCodeValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public enum MarketingType implements CommonStringCode {

    SNS("SNS", "SNS"),
    EMAIL("EMAIL", "이메일"),
    EMPTY(null, "");

    private final String code;
    private final String desc;

    public static boolean hasCode(String code) {
        return Arrays.stream(MarketingType.values()).anyMatch(c -> c.code.equals(code));
    }

    public static MarketingType getMarketingType(String code) {
        return Arrays.stream(MarketingType.values()).filter(c -> c.code.equals(code)).findFirst().orElse(EMPTY);
    }

    public static List<EnumStringCodeValue> getMarketingTypeList() {
        return Arrays.stream(MarketingType.values()).filter(c -> c.code != null).map(EnumStringCodeValue::new).collect(Collectors.toList());
    }
}
