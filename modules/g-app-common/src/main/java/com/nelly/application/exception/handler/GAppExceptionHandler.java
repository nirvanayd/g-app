package com.nelly.application.exception.handler;

import com.nelly.application.dto.Response;
import com.nelly.application.exception.*;
import com.nelly.application.exception.enums.ExceptionCode;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GAppExceptionHandler {

    private final Response response;

    @ExceptionHandler(ExpireTokenException.class)
    public ResponseEntity<?> handleExpireTokenException() {
        log.info(">>> AuthenticationException");
        ExceptionCode exceptionCode = ExceptionCode.EXPIRED_TOKEN_EXCEPTION;
        return response.fail(exceptionCode.getCode(), exceptionCode.getMessage(), exceptionCode.getStatus());
    }

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

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exception) {
        ExceptionCode exceptionCode = ExceptionCode.SYSTEM_EXCEPTION;
        return response.fail(exceptionCode.getCode(), exception.getMessage(), exceptionCode.getStatus());
    }

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseEntity<?> handleFileSizeLimitExceededException(FileSizeLimitExceededException exception) {
        ExceptionCode exceptionCode = ExceptionCode.SYSTEM_EXCEPTION;
        return response.fail(exceptionCode.getCode(), exception.getMessage(), exceptionCode.getStatus());
    }

    @ExceptionHandler(SystemException.class)
    public ResponseEntity<?> handleSystemException(SystemException exception) {
        log.info(">>> SystemException");
        exception.printStackTrace();
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

    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<?> handleRuntimeException(NoContentException exception) {
        ExceptionCode exceptionCode = exception.getExceptionCode();
        return response.fail(exceptionCode.getCode(), exception.getMessage(), exceptionCode.getStatus());
    }

    @ExceptionHandler(SocialAuthenticationException.class)
    public ResponseEntity<?> handleRuntimeException(SocialAuthenticationException exception) {
        ExceptionCode exceptionCode = exception.getExceptionCode();
        if (exception.getData() != null) {
            return response.fail(exception.getData(), exceptionCode.getCode(), exception.getMessage(), exceptionCode.getStatus());
        }
        return response.fail(exceptionCode.getCode(), exception.getMessage(), exceptionCode.getStatus());
    }
}
