package com.nelly.application.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AgreementType {
    MARKETING_POLICY("marketing", "마케팅 동의"),
    PRIVACY_POLICY("privacy", "개인정보 처리방침");

    private final String code;
    private final String desc;
}
