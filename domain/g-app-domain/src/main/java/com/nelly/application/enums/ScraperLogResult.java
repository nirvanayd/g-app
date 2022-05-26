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
public enum ScraperLogResult implements CommonStringCode {
    INIT("I", "init"),
    SUCCESS("S", "성공"),
    FAILURE("F", "실패"),
    EMPTY(null, "");

    private final String code;
    private final String desc;

    public static boolean hasCode(String code) {
        return Arrays.stream(ScraperLogResult.values()).anyMatch(c -> c.code.equals(code));
    }

    public static boolean hasCodeValueExist(String code) {
        if (code == null || "".equals(code)) return true;
        return hasCode(code);
    }

    public static ScraperLogResult getScraperLogResult(String code) {
        return Arrays.stream(ScraperLogResult.values()).filter(c -> c.code.equals(code)).findFirst().orElse(EMPTY);
    }

    public static List<EnumStringCodeValue> getScraperLogResultList() {
        return Arrays.stream(ScraperLogResult.values()).filter(c -> c.code != null).map(EnumStringCodeValue::new).collect(Collectors.toList());
    }
}
