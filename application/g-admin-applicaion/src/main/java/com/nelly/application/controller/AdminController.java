package com.nelly.application.controller;

import com.nelly.application.domain.Users;
import com.nelly.application.dto.LoginRequestDto;
import com.nelly.application.dto.Response;
import com.nelly.application.dto.SignUpRequestDto;
import com.nelly.application.dto.response.LoginResponse;
import com.nelly.application.dto.response.UserTestResponse;
import com.nelly.application.service.admin.AdminService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@AllArgsConstructor
@Service
@Slf4j
public class AdminController {

    private final AdminService adminService;
    private final Response response;
    private final ModelMapper modelMapper;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpRequestDto dto) {
        adminService.signUp(dto);
        return response.success();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDto dto) {
        String accessToken = adminService.login(dto.getLoginId(), dto.getPassword());
        LoginResponse data = LoginResponse.builder().accessToken(accessToken).build();
        return response.success(data);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String bearerToken ) {
        String token = adminService.getToken(bearerToken);
        adminService.logout(token);
        return response.success();
    }

    @GetMapping("/admin-test")
    public ResponseEntity<?> userTest(@RequestHeader("Authorization") String bearerToken ) {
        String token = adminService.getToken(bearerToken);
        Users user = adminService.userTest(token);
        UserTestResponse userTestResponse = modelMapper.map(user, UserTestResponse.class);
        return response.success(userTestResponse);
    }
}
