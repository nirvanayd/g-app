package com.nelly.application.exception;

import com.nelly.application.exception.enums.ExceptionCode;

public class NoContentException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public NoContentException(String message) {
        super(message);
        this.exceptionCode = ExceptionCode.NO_CONTENT;
    }

    public NoContentException() {
        super();
        this.exceptionCode = ExceptionCode.NO_CONTENT;
    }

    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }
}
