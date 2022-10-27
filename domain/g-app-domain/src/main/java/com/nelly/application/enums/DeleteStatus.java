package com.nelly.application.enums;

import com.nelly.application.enums.enumInterface.CommonIntegerCode;
import com.nelly.application.enums.enumInterface.CommonStringCode;
import dto.EnumIntegerCodeValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public enum DeleteStatus implements CommonStringCode {
    NORMAL("0", "정상"),
    WRITER("1", "작성자"),
    CONTENT_WRITER("2", "원게시글작성자"),
    ADMIN("3", "관리자"),
    EMPTY(null, "");

    private final String code;
    private final String desc;

    public static boolean hasCode(String code) {
        return Arrays.stream(DeleteStatus.values()).anyMatch(c -> c.code.equals(code));
    }

    public static boolean hasCodeValueExist(String code) {
        if (code == null) return true;
        return hasCode(code);
    }

    public static DeleteStatus getDeleteStatus(String code) {
        return Arrays.stream(DeleteStatus.values()).filter(c -> c.code.equals(code)).findFirst().orElse(EMPTY);
    }
}
