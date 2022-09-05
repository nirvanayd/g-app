package com.nelly.application.exception.enums;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum ExceptionCode {
    // 4xx
    NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "NOT FOUND EXCEPTION", ""),
    AUTHENTICATION_EXCEPTION(HttpStatus.GONE, "AUTHENTICATION EXCEPTION", "토큰 정보가 올바르지 않습니다."),
    ACCESS_DENIED_EXCEPTION(HttpStatus.GONE, "ACCESS DENIED EXCEPTION", "접근 권한이 없습니다."),
    EXPIRED_TOKEN_EXCEPTION(HttpStatus.LENGTH_REQUIRED, "EXPIRED TOKEN EXCEPTION", "토큰이 만료되었습니다."),
    SCRAPER_EXCEPTION(HttpStatus.NOT_EXTENDED, "SCRAPER EXCEPTION", "제품 정보 수집 중 오류가 발생했습니다."),
    // 5xx
    SYSTEM_EXCEPTION(HttpStatus.NOT_EXTENDED, "SYSTEM EXCEPTION", "시스템 오류"),
    // 이하 Parameter validation exception handler
    DTO_VALIDATION_EXCEPTION(HttpStatus.NOT_EXTENDED, "VALIDATION EXCEPTION", "데이터 유효성 검사 오류"),
    CONSTRAINT_VIOLATION_EXCEPTION(HttpStatus.NOT_EXTENDED, "VARIABLE EXCEPTION", "데이터 제약 조건 유효성 오류");

    private HttpStatus status;
    private String code;
    private String message;

    public HttpStatus getStatus() {return this.status; }
    public String getCode() {
        return this.code;
    }
    public String getMessage() {
        return this.message;
    }
}

