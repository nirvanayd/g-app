package com.nelly.application.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum YesOrNoType {
    YES("Y", "예"),
    NO("N", "아니오");

    private final String code;
    private final String desc;

}
