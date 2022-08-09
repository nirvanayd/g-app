package com.nelly.application.controller;

import com.nelly.application.domain.Users;
import com.nelly.application.dto.TokenInfoDto;
import com.nelly.application.dto.request.*;
import com.nelly.application.dto.Response;
import com.nelly.application.dto.response.LoginResponse;
import com.nelly.application.dto.response.UserResponse;
import com.nelly.application.enums.StyleType;
import com.nelly.application.mail.MailSender;
import com.nelly.application.service.user.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;


@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final Response response;
    private final ModelMapper modelMapper;
    private final MailSender mailSender;

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

    @PostMapping("/reissue")
    public ResponseEntity<?> userTest(@RequestBody ReissueRequest requestDto) {
        TokenInfoDto tokenInfoDto = userService.reissue(requestDto);
        LoginResponse data = LoginResponse.builder()
                .accessToken(tokenInfoDto.getAccessToken())
                .refreshToken(tokenInfoDto.getRefreshToken())
                .build();
        return response.success(data);
    }

    @GetMapping("/users/logout")
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

    /**
     * 사용자 아이디 중복확인
     */
    @PostMapping("/duplicate-id")
    public ResponseEntity<?> duplicateId(@RequestBody @Valid DuplicateIdRequest dto) {
        Optional<Users> user = userService.existId(dto.getLoginId());
        if (user.isPresent()) {
            throw new RuntimeException("이미 사용중인 아이디입니다.");
        }
        return response.success();
    }

    /**
     * 사용자 이메일 중복확인
     */
    @PostMapping("/duplicate-email")
    public ResponseEntity<?> duplicateEmail(@RequestBody @Valid DuplicateEmailRequest dto) {
        Optional<Users> user = userService.existEmail(dto.getEmail());
        if (user.isPresent()) {
            throw new RuntimeException("이미 사용중인 이메일입니다.");
        }
        return response.success();
    }

    @GetMapping("/style-list")
    public ResponseEntity<?> getStyle() {
        return response.success(StyleType.getStyleList());
    }

    @PostMapping("/find-id")
    public ResponseEntity<?> findId(@RequestBody @Valid FindIdRequest dto) {
        userService.findId(dto.getEmail());
        return response.success();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordRequest dto) {
        userService.resetPassword(dto.getEmail());
        return response.success();
    }

    @PostMapping("/users/password")
    public ResponseEntity<?> updatePassword(@RequestBody @Valid UpdatePasswordRequest dto) {
        Optional<Users> user = userService.getAppUser();
        if (user.isEmpty()) throw new RuntimeException("사용자 정보를 조회할 수 없습니다.");
        userService.updatePassword(user.get(), dto);

        return response.success();
    }

    @PostMapping("/users/email")
    public ResponseEntity<?> updateEmail(@RequestBody @Valid UpdateEmailRequest dto) {
        Optional<Users> user = userService.getAppUser();
        if (user.isEmpty()) throw new RuntimeException("사용자 정보를 조회할 수 없습니다.");

        userService.updateEmail(user.get(), dto);

        return response.success();
    }
}
