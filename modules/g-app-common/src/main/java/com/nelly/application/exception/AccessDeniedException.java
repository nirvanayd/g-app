package com.nelly.application.exception;


import com.nelly.application.exception.enums.ExceptionCode;

public class AccessDeniedException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public AccessDeniedException(String message) {
        super(message);
        this.exceptionCode = ExceptionCode.ACCESS_DENIED_EXCEPTION;
    }

    public AccessDeniedException() {
        super();
        this.exceptionCode = ExceptionCode.ACCESS_DENIED_EXCEPTION;
    }

    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }
}
