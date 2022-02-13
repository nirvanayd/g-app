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
public enum RoleType implements CommonStringCode {
    USER("ROLE_USER", "앱사용자"),
    ADMIN("ROLE_ADMIN", "관리자"),
    EMPTY(null, "");

    private final String code;
    private final String desc;

    public static boolean hasCode(String code) {
        return Arrays.stream(RoleType.values()).anyMatch(c -> c.code.equals(code));
    }

    public static boolean hasCodeValueExist(String code) {
        if (code == null || "".equals(code)) return true;
        return hasCode(code);
    }

    public static RoleType getRoleTypes(String code) {
        return Arrays.stream(RoleType.values()).filter(c -> c.code.equals(code)).findFirst().orElse(EMPTY);
    }

    public static List<EnumStringCodeValue> getRoleTypeList() {
        return Arrays.stream(RoleType.values()).filter(c -> c.code != null).map(EnumStringCodeValue::new).collect(Collectors.toList());
    }
}
