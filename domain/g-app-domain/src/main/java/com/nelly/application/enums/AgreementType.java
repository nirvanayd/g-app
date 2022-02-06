package com.nelly.application.enums;

import com.nelly.application.enums.enumInterface.CommonEnums;
import com.nelly.application.enums.enumInterface.CommonStringCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum AgreementType implements CommonStringCode {
    MARKETING_POLICY("marketing", "마케팅 동의"),
    PRIVACY_POLICY("privacy", "개인정보 처리방침"),
    EMPTY(null, "");

    private final String code;
    private final String desc;

    public static boolean hasCode(String code) {
        return Arrays.stream(AgreementType.values()).anyMatch(c -> c.code.equals(code));
    }

    public static AgreementType getAgreementType(String code) {
        return Arrays.stream(AgreementType.values()).filter(c -> c.code.equals(code)).findFirst().orElse(EMPTY);
    }
}
