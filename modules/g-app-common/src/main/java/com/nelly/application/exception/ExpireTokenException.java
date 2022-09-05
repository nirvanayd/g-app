package com.nelly.application.exception;


import com.nelly.application.exception.enums.ExceptionCode;

public class ExpireTokenException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public ExpireTokenException(String message) {
        super(message);
        this.exceptionCode = ExceptionCode.EXPIRED_TOKEN_EXCEPTION;
    }

    public ExpireTokenException() {
        super();
        this.exceptionCode = ExceptionCode.EXPIRED_TOKEN_EXCEPTION;
    }

    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }
}
