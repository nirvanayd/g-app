package com.nelly.application.controller;

import com.nelly.application.domain.UserAgreements;
import com.nelly.application.domain.Users;
import com.nelly.application.dto.TokenInfoDto;
import com.nelly.application.dto.request.*;
import com.nelly.application.dto.Response;
import com.nelly.application.dto.response.*;
import com.nelly.application.enums.StyleType;
import com.nelly.application.mail.MailSender;
import com.nelly.application.service.content.ContentService;
import com.nelly.application.service.user.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@AllArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final ContentService contentService;
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
        TokenInfoDto tokenInfoDto = userService.login(dto);

        log.info("access token --> " + tokenInfoDto.getAccessToken());
        log.info("refresh token --> " + tokenInfoDto.getRefreshToken());

        LoginResponse data = LoginResponse.builder().accessToken(tokenInfoDto.getAccessToken())
                .refreshToken(tokenInfoDto.getRefreshToken()).build();
        return response.success(data);
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> userTest(@RequestBody ReissueRequest requestDto) {
        log.info("reissue/access token --> " + requestDto.getAccessToken());
        log.info("reissue/refresh token --> " + requestDto.getRefreshToken());
        TokenInfoDto tokenInfoDto = userService.reissue(requestDto);
        LoginResponse data = LoginResponse.builder()
                .accessToken(tokenInfoDto.getAccessToken())
                .refreshToken(tokenInfoDto.getRefreshToken())
                .build();

        log.info("reissue/response access token --> " + tokenInfoDto.getAccessToken());
        log.info("reissue/response refresh token --> " + tokenInfoDto.getRefreshToken());
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
        List<UserAgreements> list = userService.getAppUserAgreements(users);
        UserResponse userResponse = modelMapper.map(users, UserResponse.class);

        List<UserAgreementsResponse> userAgreementList =
                list.stream().map(l -> modelMapper.map(l, UserAgreementsResponse.class)).collect(Collectors.toList());

        userResponse.setUserAgreements(userAgreementList);

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

    @PostMapping("/users/agreement")
    public ResponseEntity<?> updateAgreement(@RequestBody @Valid UpdateAgreementRequest dto) {
        Optional<Users> user = userService.getAppUser();
        if (user.isEmpty()) throw new RuntimeException("사용자 정보를 조회할 수 없습니다.");
        userService.updateAgreement(user.get(), dto);
        return response.success();
    }

    /**
     * 사용자 스타일 변경
     * */
    @PutMapping("/users/styles")
    public ResponseEntity<?> updateUserStyle(@RequestBody @Valid UpdateUserStyleRequest dto) {
        Optional<Users> user = userService.getAppUser();
        if (user.isEmpty()) throw new RuntimeException("사용자 정보를 조회할 수 없습니다.");
        userService.updateUserStyle(user.get(), dto);
        return response.success();
    }

    @PostMapping("/users/follow")
    public ResponseEntity<?> saveFollow(@RequestBody @Valid SaveFollowRequest dto) {
        Optional<Users> user = userService.getAppUser();
        if (user.isEmpty()) throw new RuntimeException("사용자 정보를 조회할 수 없습니다.");
        userService.saveUserFollow(user.get(), dto);
        return response.success();
    }

    @GetMapping("/users/like")
    public ResponseEntity<?> getUserLike(GetUserLikeRequest dto) {
        Optional<Users> user = userService.getAppUser();
        if (user.isEmpty()) throw new RuntimeException("사용자 정보를 조회할 수 없습니다.");
        Users appUser = user.get();
        return response.success(contentService.getUserLikeList(appUser, dto));
    }

    @GetMapping("/users/mark")
    public ResponseEntity<?> getUserMark(GetUserLikeRequest dto) {
        Optional<Users> user = userService.getAppUser();
        if (user.isEmpty()) throw new RuntimeException("사용자 정보를 조회할 수 없습니다.");
        Users appUser = user.get();
        return response.success(contentService.getUserMarkList(appUser, dto));
    }

    @GetMapping("/users/detail/{id}")
    public ResponseEntity<?> getUserDetailById(@PathVariable String id) {
        Long userDetailId = Long.parseLong(id);
        Optional<Users> user = userService.getAppUser();

        if (user.isPresent()) {
            // 토큰이 없을 때는 다른 사람 페이지
            // 토큰이 있을 때 user.id === userDetailId --> 본인 마이페이지
            // 토큰이 있을 때 user.id !== userDetailId --> 다른 사람 페이지
            if (Objects.equals(user.get().getId(), userDetailId)) {
                return response.success(userService.getUserDetailOwner(userDetailId));
            }
            return response.success(userService.getUserDetail(userDetailId, user));
        } else {
            return response.success(userService.getUserDetail(userDetailId, user));
        }
    }

    @GetMapping("/user/detail/content/{id}")
    public ResponseEntity<?> getUserDetailContentList(@PathVariable String id,
                                                      GetContentListRequest dto) {
        Long userDetailId = Long.parseLong(id);
        Optional<Users> user = userService.getAppUser();
        return response.success(userService.getUserDetailContentList(userDetailId, dto));
    }

    @GetMapping("/user/detail/mark/{id}")
    public ResponseEntity<?> getUserDetailMarkContentList(@PathVariable String id,
                                                      GetContentListRequest dto) {
        Long userDetailId = Long.parseLong(id);
        Optional<Users> user = userService.getAppUser();
        return response.success(userService.getUserDetailMarkContentList(userDetailId, dto));
    }

    @PostMapping("/users/profile-image")
    public ResponseEntity<?> saveProfileImage(@NotNull @RequestParam("image") MultipartFile image) {
        return response.success();
    }

    @PostMapping("/users/background-image")
    public ResponseEntity<?> saveBackgroundImage() {
        return response.success();
    }
}
