package com.nelly.application.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserStatus {
    NORMAL("00", "정상"),
    BLOCK("10", "사용중지"),
    LEAVE("99", "탈퇴");

    private final String code;
    private final String desc;

}
