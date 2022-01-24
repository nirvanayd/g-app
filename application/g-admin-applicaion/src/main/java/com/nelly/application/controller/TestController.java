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
        return ResponseEntity.ok("admin updated...");
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
}