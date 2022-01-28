package com.nelly.application.exception.handler;

import com.nelly.application.dto.Response;
import com.nelly.application.exception.AccessDeniedException;
import com.nelly.application.exception.AuthenticationException;
import com.nelly.application.exception.SystemException;
import com.nelly.application.exception.enums.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GAppExceptionHandler {

    private final Response response;

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException() {
        log.info(">>> AccessDeniedException");
        ExceptionCode exceptionCode = ExceptionCode.ACCESS_DENIED_EXCEPTION;
        return response.fail(exceptionCode.getCode(), exceptionCode.getMessage(), exceptionCode.getStatus());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException() {
        log.info(">>> AuthenticationException");
        ExceptionCode exceptionCode = ExceptionCode.ACCESS_DENIED_EXCEPTION;
        return response.fail(exceptionCode.getCode(), exceptionCode.getMessage(), exceptionCode.getStatus());
    }

    @ExceptionHandler(SystemException.class)
    public ResponseEntity<?> handleSystemException(SystemException exception) {
        log.info(">>> SystemException");
        ExceptionCode exceptionCode = exception.getExceptionCode();
        return response.fail(exceptionCode.getCode(), exception.getMessage(), exceptionCode.getStatus());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException exception) {
        log.info(">>> runtime exception");
        exception.printStackTrace();
        ExceptionCode exceptionCode = ExceptionCode.SYSTEM_EXCEPTION;
        return response.fail(exceptionCode.getCode(), exception.getMessage(), exceptionCode.getStatus());
    }
}
