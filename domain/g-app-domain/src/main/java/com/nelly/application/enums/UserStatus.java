package com.nelly.application.enums;

import com.nelly.application.enums.enumInterface.CommonStringCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum UserStatus implements CommonStringCode {
    NORMAL("00", "정상"),
    BLOCK("10", "사용중지"),
    LEAVE("99", "탈퇴"),
    EMPTY(null, "");

    private final String code;
    private final String desc;

    public static boolean hasCode(String code) {
        return Arrays.stream(UserStatus.values()).anyMatch(c -> c.code.equals(code));
    }

    public static UserStatus getUserStatus(String code) {
        return Arrays.stream(UserStatus.values()).filter(c -> c.code.equals(code)).findFirst().orElse(EMPTY);
    }

}
