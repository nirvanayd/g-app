package com.nelly.application.enums;

import com.nelly.application.enums.enumInterface.CommonStringCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum ScraperStatus implements CommonStringCode {
    NORMAL("00", "정상"),
    NOT_FOUND("10", "URL검색실패"),
    SCRAP_ERROR("20", "크롤링오류"),
    EMPTY(null, "");

    private final String code;
    private final String desc;

    public static boolean hasCodeValueExist(String code) {
        if (code == null || "".equals(code)) return true;
        return hasCode(code);
    }

    public static boolean hasCode(String code) {
        return Arrays.stream(ScraperStatus.values()).anyMatch(c -> c.code.equals(code));
    }

    public static ScraperStatus getScraperStatus(String code) {
        return Arrays.stream(ScraperStatus.values()).filter(c -> c.code.equals(code)).findFirst().orElse(EMPTY);
    }
}
