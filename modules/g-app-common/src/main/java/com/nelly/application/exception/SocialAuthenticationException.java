package com.nelly.application.exception;


import com.nelly.application.exception.enums.ExceptionCode;

public class SocialAuthenticationException extends RuntimeException {
    private final ExceptionCode exceptionCode;
    private Object data;

    public SocialAuthenticationException(String message) {
        super(message);
        this.exceptionCode = ExceptionCode.SOCIAL_AUTHENTICATION_EXCEPTION;
    }

    public SocialAuthenticationException() {
        super();
        this.exceptionCode = ExceptionCode.SOCIAL_AUTHENTICATION_EXCEPTION;
    }

    public SocialAuthenticationException(String message, Object data) {
        super(message);
        this.data = data;
        this.exceptionCode = ExceptionCode.SOCIAL_AUTHENTICATION_EXCEPTION;
    }

    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }

    public Object getData() {return data;}
}
