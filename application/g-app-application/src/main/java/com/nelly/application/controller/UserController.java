package com.nelly.application.controller;

import com.nelly.application.domain.Users;
import com.nelly.application.dto.LoginRequestDto;
import com.nelly.application.dto.Response;
import com.nelly.application.dto.SignUpRequestDto;
import com.nelly.application.dto.response.LoginResponse;
import com.nelly.application.dto.response.UserTestResponse;
import com.nelly.application.service.user.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.management.modelmbean.ModelMBean;
import javax.validation.Valid;


@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
@Service
public class UserController {

    private final UserService userService;
    private final Response response;
    private final ModelMapper modelMapper;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpRequestDto dto) {
        userService.signUp(dto);
        return response.success();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDto dto) {
        String accessToken = userService.login(dto.getLoginId(), dto.getPassword());
        LoginResponse data = LoginResponse.builder().accessToken(accessToken).build();
        return response.success(data);
    }

    @GetMapping("/authority")
    public ResponseEntity<?> authority() {
        userService.authority();
        return response.success();
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String bearerToken ) {
        String token = userService.getToken(bearerToken);
        userService.logout(token);
        return response.success();
    }

    @GetMapping("/user-test")
    public ResponseEntity<?> userTest(@RequestHeader("Authorization") String bearerToken ) {
        String token = userService.getToken(bearerToken);
        Users user = userService.userTest(token);

        UserTestResponse userTestResponse = modelMapper.map(user, UserTestResponse.class);

        return response.success(userTestResponse);
    }
}
