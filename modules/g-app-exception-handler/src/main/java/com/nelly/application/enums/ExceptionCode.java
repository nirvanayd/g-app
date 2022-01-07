package com.nelly.application.enums;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum ExceptionCode {
    // 4xx
    NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "NOT_FOUND_EXCEPTION", ""),
    S_AUTHENTICATION_EXCEPTION(HttpStatus.GONE, "AUTHENTICATION_EXCEPTION", "인증 오류"),
    TOKEN_NOT_FOUND(HttpStatus.GONE, "TOKEN_NOT_FOUND", "토큰 정보가 유효하지 않습니다."),
    // 5xx
    VALIDATION_EXCEPTION(HttpStatus.NOT_EXTENDED, "VALIDATION_EXCEPTION", "유효성 검사 오류"),
    WEB_CLIENT_CONNECT_EXCEPTION(HttpStatus.NOT_EXTENDED, "WEB_CLIENT_CONNECT_EXCEPTION", "웹 클라이언트"),
    NO_SEARCH_RESULT_EXCEPTION(HttpStatus.NOT_EXTENDED, "NO_SEARCH_RESULT_EXCEPTION", ""),
    DATABASE_EXCEPTION(HttpStatus.NOT_EXTENDED, "DATABASE_EXCEPTION", "데이터베이스 오류"),
    DATABASE_CACHE_EXCEPTION(HttpStatus.NOT_EXTENDED, "DATABASE_REDIS_EXCEPTION", "캐시 데이터베이스 오류"),
    AMAZON_SERVICE_EXCEPTION(HttpStatus.NOT_EXTENDED, "AMAZON_SERVICE_EXCEPTION", "아마존 서비스 연동 오류"),
    PAYMENT_SERVICE_EXCEPTION(HttpStatus.NOT_EXTENDED, "PAYMENT_SERVICE_EXCEPTION", "결제 서비스 연동 오류"),
    SYSTEM_EXCEPTION(HttpStatus.NOT_EXTENDED, "SYSTEM_EXCEPTION", "시스템 오류"),
    // 이하 Parameter validation exception handler
    DTO_VALIDATION_EXCEPTION(HttpStatus.NOT_EXTENDED, "DTO_VALIDATION_EXCEPTION", "데이터 유효성 검사 오류"),
    CONSTRAINT_VIOLATION_EXCEPTION(HttpStatus.NOT_EXTENDED, "PATH_VARIABLE_EXCEPTION", "데이터 제약 조건 유효성 오류");

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
