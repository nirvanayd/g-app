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

        private String result;
        private String massage;
        private Object data;
        private Object error;
    }

    public ResponseEntity<?> success(Object data, String msg, HttpStatus status) {
        Body body = Body.builder()
                .data(data)
                .result("success")
                .massage(msg)
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

}
