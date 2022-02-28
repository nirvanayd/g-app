package com.nelly.application.enums;

import com.nelly.application.enums.enumInterface.CommonStringCode;
import dto.EnumStringCodeValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public enum HashTagType implements CommonStringCode {
    USER("user", "사용자"),
    BRAND("brand", "브랜드"),
    ITEM("item", "제품"),
    EMPTY(null, "");

    private final String code;
    private final String desc;

    public static boolean hasCode(String code) {
        return Arrays.stream(HashTagType.values()).anyMatch(c -> c.code.equals(code));
    }

    public static boolean hasCodeValueExist(String code) {
        if (code == null || "".equals(code)) return true;
        return hasCode(code);
    }

    public static HashTagType getHashTagType(String code) {
        return Arrays.stream(HashTagType.values()).filter(c -> c.code.equals(code)).findFirst().orElse(EMPTY);
    }

    public static List<EnumStringCodeValue> getHashTagTypeList() {
        return Arrays.stream(HashTagType.values()).filter(c -> c.code != null).map(EnumStringCodeValue::new).collect(Collectors.toList());
    }
}
