package com.nelly.application.dto;

import com.nelly.application.enums.ExceptionCode;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

@Setter
public class ExceptionResponseDto {
    private HttpStatus status;
    private String code;
    private String message;
    private List<FieldError> fieldErrors = new ArrayList<>(); // exception list 배열 저장

    public ExceptionResponseDto(ExceptionCode exceptionCode) {
        this.status = exceptionCode.getStatus();
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }

    public ExceptionResponseDto(ExceptionCode exceptionCode, String message) {
        this.status = exceptionCode.getStatus();
        this.code = exceptionCode.getCode();
        this.message = message;
    }

//    public HttpStatus getStatus() {
//        return status;
//    }

    private HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
