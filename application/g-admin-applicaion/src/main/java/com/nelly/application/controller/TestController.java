package com.nelly.application.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Value("${spring.config.activate.on-profile}")
    private String env;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("master-admin test");
    }

    @GetMapping("/env")
    public ResponseEntity<String> env() {
        return ResponseEntity.ok("this env is " + env);
    }
}