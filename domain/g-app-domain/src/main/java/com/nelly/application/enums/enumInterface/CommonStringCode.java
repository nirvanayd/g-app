package com.nelly.application.enums.enumInterface;

/**
 * code, desc 는 공용으로 사용.
 * annotaion을 통한 유효성 검사 시에는 해당 Interface를 상속받아야함.
 */
public interface CommonStringCode extends CommonEnums{
    String getCode();
    String getDesc();
//    boolean hasCode(String code);
}
