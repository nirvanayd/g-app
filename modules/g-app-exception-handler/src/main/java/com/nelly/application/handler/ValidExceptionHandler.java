package com.nelly.application.handler;

import com.nelly.application.dto.ExceptionResponseDto;
import com.nelly.application.enums.ExceptionCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;

@ControllerAdvice
public class ValidExceptionHandler {
    /**
     * DTO 검증 실패
     *
     * @param methodArgumentNotValidException exception
     * @return ErrorDto
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ExceptionResponseDto> handleDtoValidationException(
            MethodArgumentNotValidException methodArgumentNotValidException) {

        ExceptionCode exceptionCode = ExceptionCode.DTO_VALIDATION_EXCEPTION;
        String message = this.getMessage(methodArgumentNotValidException.getBindingResult());
        return new ResponseEntity<>(this.getResponseDto(exceptionCode, message),
                exceptionCode.getStatus());
    }


    /**
     * annotaion 변수 type 예외
     *
     * @param methodArgumentNotValidException
     * @return
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponseDto> handleMethodArgumentTypeMismatchException (
            MethodArgumentTypeMismatchException methodArgumentNotValidException) {

        ExceptionCode exceptionCode = ExceptionCode.DTO_VALIDATION_EXCEPTION;
        String message = methodArgumentNotValidException.getMessage();

        return new ResponseEntity<>(this.getResponseDto(exceptionCode, message),
                exceptionCode.getStatus());
    }

    /**
     * DTO Validation 검증 실패 메시지를 만들기 위한 함수
     *
     * @param bindingResult validation 검증 결과
     * @return String
     */
    private String getMessage(BindingResult bindingResult) {
        String field = bindingResult.getFieldErrors().get(0).getField();
        String message = bindingResult.getFieldErrors().get(0).getDefaultMessage();
        return "[" + field + "] " + message;
    }

    /**
     * PathVariable Validation 검증 실패 메시지를 만들기 위한 함수
     *
     * @param violation validation 검증 결과
     * @return String
     */
    private String getMessage(ConstraintViolation<?> violation) {
        String field = violation.getPropertyPath().toString();
        String message = violation.getMessage();
        return "[" + field + "] " + message;
    }

    private ExceptionResponseDto getResponseDto(ExceptionCode exceptionCode, String message) {
        return new ExceptionResponseDto(exceptionCode, message);
    }

    private ExceptionResponseDto getResponseDto(ExceptionCode exceptionCode) {
        String message = "시스템 오류";
        return new ExceptionResponseDto(exceptionCode, message);
    }
}
