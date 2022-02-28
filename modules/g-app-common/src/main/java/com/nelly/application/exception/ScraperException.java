package com.nelly.application.exception;

import com.nelly.application.exception.enums.ExceptionCode;

public class ScraperException extends RuntimeException{
    private final ExceptionCode exceptionCode;

    public ScraperException(String message) {
        super(message);
        this.exceptionCode = ExceptionCode.ACCESS_DENIED_EXCEPTION;
    }

    public ScraperException() {
        super();
        this.exceptionCode = ExceptionCode.ACCESS_DENIED_EXCEPTION;
    }

    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }
}
