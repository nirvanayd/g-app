package com.nelly.application.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
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
        return success(null, null, HttpStatus.OK);
    }

    /**
     * List Response 대응
     */
    public ResponseEntity<?> success(ResponseData data, String msg, HttpStatus status) {
        Body body = Body.builder()
                .data(data)
                .code("success")
                .message(msg)
                .error(Collections.emptyList())
                .build();
        if (data instanceof CommonListResponse) {
            if (data.isEnded()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(body);
            }
        }
        return ResponseEntity.status(status).body(body);
    }

    public ResponseEntity<?> success(ResponseData data) {
        return success(data, null, HttpStatus.OK);
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

    public String convertToJson(Object data, String code, String msg) {
        Body body = Body.builder()
                .data(data)
                .code(code)
                .message(msg)
                .build();
        Gson gson = new Gson();
        return gson.toJson(body);
    }
}
