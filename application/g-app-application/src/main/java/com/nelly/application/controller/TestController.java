package com.nelly.application.controller;

import com.nelly.application.dto.request.SearchRequest;
import com.nelly.application.service.content.ContentService;
import com.nelly.application.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final ContentService contentService;
    private final UserService userService;

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

    @GetMapping("/reset-block")
    public ResponseEntity<String> resetBlock() {
        contentService.resetBlock();
        contentService.resetReport();
        userService.resetBlock();
        return ResponseEntity.ok("success");
    }
}
