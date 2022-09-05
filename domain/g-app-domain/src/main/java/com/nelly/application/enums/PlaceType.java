package com.nelly.application.enums;

import com.nelly.application.enums.enumInterface.CommonEnums;
import com.nelly.application.enums.enumInterface.CommonStringCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public enum PlaceType implements CommonStringCode {

    FIELD("field", "필드"),
    PRACTICE("practice", "연습"),
    SCREEN("screen", "스크린"),
    EMPTY(null, "");

    private final String code;
    private final String desc;

    public static boolean hasCode(String code) {
        return Arrays.stream(PlaceType.values()).anyMatch(c -> c.code.equals(code));
    }

    public static PlaceType getPlaceType(String code) {
        return Arrays.stream(PlaceType.values()).filter(c -> c.code.equals(code)).findFirst().orElse(EMPTY);
    }

    public static List<PlaceType> getPlaceList(List<String> codeList) {
        if (codeList == null || codeList.isEmpty()) return Collections.emptyList();
        return Arrays.stream(PlaceType.values()).filter(c -> c.code != null & codeList.contains(c.code)).collect(Collectors.toList());
    }
}
