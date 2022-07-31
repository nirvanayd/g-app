package com.nelly.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {
    @Value("${spring.config.activate.on-profile}")
    private String env;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok(env + "###hello###");
    }

    @GetMapping("/env")
    public ResponseEntity<String> env() {

        log.info("env test..");
        return ResponseEntity.ok("this env is -->" + env);
    }
}
