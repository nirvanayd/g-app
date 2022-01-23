package com.nelly.application.exception.handler;

import com.nelly.application.dto.Response;
import com.nelly.application.exception.enums.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ValidExceptionHandler {

    private final Response response;

    /**
     * DTO 검증 실패
     *
     * @param methodArgumentNotValidException exception
     * @return ErrorDto
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<?> handleDtoValidationException(
            MethodArgumentNotValidException methodArgumentNotValidException) {

        ExceptionCode exceptionCode = ExceptionCode.DTO_VALIDATION_EXCEPTION;
        String message = this.getMessage(methodArgumentNotValidException.getBindingResult());

        return response.fail(exceptionCode.getCode(), message, exceptionCode.getStatus());
    }


    /**
     * annotaion 변수 type 예외
     *
     * @param methodArgumentNotValidException
     * @return
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMethodArgumentTypeMismatchException (
            MethodArgumentTypeMismatchException methodArgumentNotValidException) {

        ExceptionCode exceptionCode = ExceptionCode.DTO_VALIDATION_EXCEPTION;
        String message = methodArgumentNotValidException.getMessage();

        return response.fail(exceptionCode.getCode(), message, exceptionCode.getStatus());
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
}
