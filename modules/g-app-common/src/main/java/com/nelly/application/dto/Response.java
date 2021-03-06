package com.nelly.application.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class Response {
    @Getter
    @Builder
    private static class Body {

        private String code;
        private String message;
        private Object data;
        private Object error;
    }

    /**
     * success
     */
    public ResponseEntity<?> success(Object data, String msg, HttpStatus status) {
        Body body = Body.builder()
                .data(data)
                .code("success")
                .message(msg)
                .error(Collections.emptyList())
                .build();
        return ResponseEntity.ok(body);
    }

    public ResponseEntity<?> success(String msg) {
        return success(Collections.emptyList(), msg, HttpStatus.OK);
    }

    public ResponseEntity<?> success(Object data) {
        return success(data, null, HttpStatus.OK);
    }

    public ResponseEntity<?> success() {
        return success(Collections.emptyList(), null, HttpStatus.OK);
    }

    /**
     * fail
     */
    public ResponseEntity<?> fail(Object data, String code, String msg, HttpStatus status) {
        Body body = Body.builder()
                .data(data)
                .code(code)
                .message(msg)
                .build();
        return ResponseEntity.status(status).body(body);
    }

    public ResponseEntity<?> fail(Object data, String msg, HttpStatus status) {
        return fail(Collections.emptyList(), "fail", msg, status);
    }

    public ResponseEntity<?> fail(String code, String msg, HttpStatus status) {
        return fail(Collections.emptyList(), code, msg, status);
    }

    public ResponseEntity<?> fail(String msg, HttpStatus status) {
        return fail(Collections.emptyList(), msg, status);
    }

    public ResponseEntity<?> fail(HttpStatus status) {
        return fail(Collections.emptyList(), "fail", null, status);
    }

    public ResponseEntity<?> fail() {
        return fail(Collections.emptyList(), "fail", null, HttpStatus.NOT_EXTENDED);
    }


}
