package com.nelly.application.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AgeType {
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
    EMPTY("", "");

    private final String code;
    private final String desc;


}
