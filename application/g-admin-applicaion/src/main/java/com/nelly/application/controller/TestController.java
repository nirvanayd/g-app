package com.nelly.application.controller;

import com.nelly.application.dto.Response;
import com.nelly.application.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final Response response;
    private final S3Uploader s3Uploader;

    @Value("${spring.config.activate.on-profile}")
    private String env;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello admin");
    }

    @GetMapping("/env")
    public ResponseEntity<String> env() {
        return ResponseEntity.ok("this env is " + env);
    }

    @GetMapping("/logger")
    public ResponseEntity<String> logger() {
        log.debug("debug log...");
        log.info("info log...");
        log.warn("warn log...");
        log.error("error log...");
        return ResponseEntity.ok("this logger test");
    }

    @PostMapping("/file-upload/test")
    public ResponseEntity<?> fileUpload(@RequestParam("images") MultipartFile multipartFile) throws IOException {
        String result = s3Uploader.upload(multipartFile, "static");
        log.info("#####");
        log.info(result);
        return response.success();
    }
}