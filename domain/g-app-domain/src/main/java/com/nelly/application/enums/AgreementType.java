package com.nelly.application.enums;

import com.nelly.application.enums.enumInterface.CommonEnums;
import com.nelly.application.enums.enumInterface.CommonStringCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum AgreementType implements CommonStringCode {
    MARKETING_POLICY("marketing", "마케팅약관"),
    PRIVACY_POLICY("privacy", "개인정보 수집이용 약관"),
    SERVICE_POLICY("service", "서비스 이용약관"),
    CHILDREN_POLICY("children", "14세 이상약관"),
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
