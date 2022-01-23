package com.nelly.application.exception;

import com.nelly.application.exception.enums.ExceptionCode;

public class AuthenticationException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public AuthenticationException(String message) {
        super(message);
        this.exceptionCode = ExceptionCode.ACCESS_DENIED_EXCEPTION;
    }

    public AuthenticationException() {
        super();
        this.exceptionCode = ExceptionCode.ACCESS_DENIED_EXCEPTION;
    }

    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }
}
