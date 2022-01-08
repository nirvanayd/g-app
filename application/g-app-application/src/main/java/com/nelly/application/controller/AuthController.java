package com.nelly.application.controller;

import com.nelly.application.dto.LoginRequestDto;
import com.nelly.application.dto.SignUpRequestDto;
import com.nelly.application.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Service
public class AuthController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody @Valid SignUpRequestDto dto) {
        userService.signUp(dto.getEmail(), dto.getPassword());
        return ResponseEntity.ok("signup");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequestDto dto) {
        userService.login(dto.getEmail(), dto.getPassword());
        return ResponseEntity.ok("login..");
    }
}
