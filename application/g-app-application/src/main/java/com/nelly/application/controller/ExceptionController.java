package com.nelly.application.controller;

import com.nelly.application.exception.AccessDeniedException;
import com.nelly.application.exception.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ExceptionController {

    @GetMapping("/exception/accessDenied")
    public void accessDeniedController() {
        throw new AccessDeniedException();
    }

    @GetMapping("/exception/entryPoint")
    public void entryPointController() {
        throw new AuthenticationException();
    }
}
