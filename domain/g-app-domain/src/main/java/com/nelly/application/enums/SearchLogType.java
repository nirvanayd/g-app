package com.nelly.application.enums;

import com.nelly.application.enums.enumInterface.CommonIntegerCode;
import com.nelly.application.enums.enumInterface.CommonStringCode;
import dto.EnumStringCodeValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public enum SearchLogType implements CommonIntegerCode {
    ACCOUNT(1, "계정"),
    BRAND(2, "브랜드"),
    TAG(3, "태그"),
    EMPTY(null, "");

    private final Integer code;
    private final String desc;

}
