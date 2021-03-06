package com.nelly.application.exception;

import com.nelly.application.exception.enums.ExceptionCode;

public class SystemException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public SystemException(String message) {
        super(message);
        this.exceptionCode = ExceptionCode.ACCESS_DENIED_EXCEPTION;
    }

    public SystemException() {
        super();
        this.exceptionCode = ExceptionCode.ACCESS_DENIED_EXCEPTION;
    }

    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }
}
