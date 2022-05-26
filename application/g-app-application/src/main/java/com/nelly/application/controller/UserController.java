package com.nelly.application.controller;

import com.nelly.application.domain.Users;
import com.nelly.application.dto.request.LoginRequest;
import com.nelly.application.dto.Response;
import com.nelly.application.dto.request.SignUpRequest;
import com.nelly.application.dto.request.UpdateUserRequest;
import com.nelly.application.dto.response.LoginResponse;
import com.nelly.application.dto.response.UserResponse;
import com.nelly.application.service.user.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final Response response;
    private final ModelMapper modelMapper;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpRequest dto) {
        userService.signUp(dto);
        return response.success();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest dto) {
        String accessToken = userService.login(dto.getLoginId(), dto.getPassword());
        LoginResponse data = LoginResponse.builder().accessToken(accessToken).build();
        return response.success(data);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String bearerToken ) {
        String token = userService.getToken(bearerToken);
        userService.logout(token);
        return response.success();
    }

    /**
     * 사용자 정보
     * DB와 동기화 해야하는 경우에 사용함.
     * @return
     */
    @GetMapping("/users")
    public ResponseEntity<?> getUser() {
        Users users = userService.getUser();
        UserResponse userResponse = modelMapper.map(users, UserResponse.class);
        return response.success(userResponse);
    }

    /**
     * 사용자 기본 정보 ( 프로필 이미지 URL, 게시물 갯수, 팔로워, 팔로잉, 프로필 타이틀, 프로필 텍스트 )
     * @return
     */
    @GetMapping("/users/default")
    public ResponseEntity<?> getUserDefault() {
        userService.getUserDefault();
        return response.success();
    }

    @GetMapping("/users/detail")
    public ResponseEntity<?> getUserDetail() {
        Users users = userService.getUser();
        return response.success(users);
    }

    @PutMapping("/users")
    public ResponseEntity<?> updateUser(@RequestBody @Valid UpdateUserRequest dto) {

        return response.success("debug...");
    }
}
