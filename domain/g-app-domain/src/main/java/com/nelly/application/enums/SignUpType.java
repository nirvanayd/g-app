package com.nelly.application.enums;

import com.nelly.application.enums.enumInterface.CommonStringCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum SignUpType implements CommonStringCode {
    EMAIL("E", "Email"),
    KAKAO("K", "Kakao"),
    APPLE("A", "Apple"),
    NAVER("N", "Naver");

    private final String code;
    private final String desc;

    public static boolean hasCode(String code) {
        return Arrays.stream(SignUpType.values()).anyMatch(c -> c.getCode().equals(code));
    }
}
