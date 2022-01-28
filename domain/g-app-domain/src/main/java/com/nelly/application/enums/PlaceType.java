package com.nelly.application.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PlaceType {

    FIELD("Field", "필드"),
    PRACTICE("Practice", "연습"),
    SCREEN("Screen", "스크린");

    private final String code;
    private final String desc;


}
