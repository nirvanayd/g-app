package com.nelly.application.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BrandStatus {
    NORMAL("00", "정상"),
    BLOCK("10", "점검"),
    LEAVE("99", "미사용");

    private final String code;
    private final String desc;
}
