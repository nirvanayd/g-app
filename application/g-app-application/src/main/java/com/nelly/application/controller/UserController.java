package com.nelly.application.controller;

import com.nelly.application.domain.Agreements;
import com.nelly.application.domain.UserAgreements;
import com.nelly.application.domain.UserStyles;
import com.nelly.application.domain.Users;
import com.nelly.application.dto.TokenInfoDto;
import com.nelly.application.dto.request.*;
import com.nelly.application.dto.Response;
import com.nelly.application.dto.response.*;
import com.nelly.application.enums.StyleType;
import com.nelly.application.exception.SystemException;
import com.nelly.application.mail.MailSender;
import com.nelly.application.service.app.AppService;
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
import java.io.IOException;
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
    private final AppService appService;
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

        // 상태 체크
        Optional<Users> loginUser = userService.getAppUser(tokenInfoDto.getAuthId());
        loginUser.ifPresent(l -> userService.checkAuthAppUser(loginUser.get()));
        LoginResponse data = LoginResponse.builder().accessToken(tokenInfoDto.getAccessToken())
                .refreshToken(tokenInfoDto.getRefreshToken()).build();
        return response.success(data);
    }

    @PostMapping(" /reissue")
    public ResponseEntity<?> userTest(@RequestBody ReissueRequest requestDto) {
        log.info("reissue/access token --> " + requestDto.getAccessToken());
        log.info("reissue/refresh token --> " + requestDto.getRefreshToken());
        TokenInfoDto tokenInfoDto = userService.reissue(requestDto);
        LoginResponse data = LoginResponse.builder()
                .accessToken(tokenInfoDto.getAccessToken())
                .refreshToken(tokenInfoDto.getRefreshToken())
                .build();

//        log.info("reissue/response access token --> " + tokenInfoDto.getAccessToken());
//        log.info("reissue/response refresh token --> " + tokenInfoDto.getRefreshToken());
        return response.success(data);
    }

    @GetMapping("/users/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String bearerToken ) {
        String token = userService.getToken(bearerToken);
        Optional<Users> user = userService.getAppUser();
        if (user.isEmpty()) {
            // user 정보 조회되지 않으면 진행하지 않음.
            return response.success();
        }
        userService.logout(user.get(), token);
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
        return response.success(userService.getUserDetailOwner(users.getId()));
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
        return response.success(userService.getUserDetail(userDetailId, user));
    }

    @GetMapping("/user/detail/content")
    public ResponseEntity<?> getOwnerUserDetailContentList(GetContentListRequest dto) {
        return response.success(userService.getOwnerUserDetailContentList(dto));
    }

    @GetMapping("/user/detail/content/{id}")
    public ResponseEntity<?> getUserDetailContentList(@PathVariable String id,
                                                      GetContentListRequest dto) {
        Long userDetailId = Long.parseLong(id);
        return response.success(userService.getUserDetailContentList(userDetailId, dto));
    }

    @GetMapping("/user/detail/mark")
    public ResponseEntity<?> getUserDetailMarkContentList(GetContentListRequest dto) {
        return response.success(userService.getOwnerUserDetailMarkContentList(dto));
    }

    @GetMapping("/user/detail/mark/{id}")
    public ResponseEntity<?> getUserDetailMarkContentList(@PathVariable String id,
                                                      GetContentListRequest dto) {
        Long userDetailId = Long.parseLong(id);
        Optional<Users> user = userService.getAppUser();
        return response.success(userService.getUserDetailMarkContentList(userDetailId, dto));
    }

    @PostMapping("/users/upload-profile")
    public ResponseEntity<?> uploadProfileImage(@NotNull @RequestParam("images") List<MultipartFile> images) throws IOException {
        ImageResponse imageResponse = userService.uploadUserProfileImage(images);
        return response.success(imageResponse);
    }

    @PostMapping("/users/upload-background")
    public ResponseEntity<?> uploadBackgroundImage(@NotNull @RequestParam("images") List<MultipartFile> images) throws IOException {
        ImageResponse imageResponse = userService.uploadUserProfileImage(images);
        return response.success(imageResponse);
    }

    @PostMapping("/users/profile-image")
    public ResponseEntity<?> saveProfileImage(@RequestBody SaveProfileImageRequest dto){
        userService.saveUserProfileImage(dto);
        return response.success();
    }

    @PostMapping("/users/background-image")
    public ResponseEntity<?> saveBackgroundImage(@RequestBody SaveBackgroundImageRequest dto) throws IOException {
        userService.saveUserBackgroundImage(dto);
        return response.success();
    }

    @PostMapping("/users/profile-title")
    public ResponseEntity<?> saveProfileTitle(@RequestBody @Valid SaveProfileTitleRequest dto) {
        userService.saveUserProfileTitle(dto);
        return response.success();
    }

    @PostMapping("/users/profile-text")
    public ResponseEntity<?> saveProfileText(@RequestBody @Valid SaveProfileTextRequest dto) {
        userService.saveUserProfileText(dto);
        return response.success();
    }

    @GetMapping("/users/agreement-list")
    public ResponseEntity<?> getUserAgreementList() {
        Users users = userService.getUser();
        List<Agreements> appAgreementList = appService.getAppAgreementList("1.0.0");
        List<UserAgreements> list = userService.getAppUserAgreements(users);
        List<UserAgreementsResponse> userAgreementList =
                list.stream().map(l -> modelMapper.map(l, UserAgreementsResponse.class)).collect(Collectors.toList());
        return response.success(userAgreementList);
    }

    @GetMapping("/users/style-list")
    public ResponseEntity<?> getUserStyleList() {
        Users user = userService.getUser();
        List<UserStyles> list = user.getUserStyles();
        List<UserStylesResponse> userStyleList =
                list.stream().map(l -> modelMapper.map(l, UserStylesResponse.class)).collect(Collectors.toList());
        return response.success(userStyleList);
    }

    @GetMapping("/users/follower-list")
    public ResponseEntity<?> getUserFollowerList(GetFollowerListRequest dto) {
        Optional<Users> user = userService.getAppUser();
        if (user.isEmpty()) throw new RuntimeException("사용자 정보를 조회할 수 없습니다.");
        return response.success(userService.getUserFollowerList(user.get(), dto));
    }

    @GetMapping("/users/following-list")
    public ResponseEntity<?> getUserFollowingList(GetFollowingListRequest dto) {
        Optional<Users> user = userService.getAppUser();
        if (user.isEmpty()) throw new RuntimeException("사용자 정보를 조회할 수 없습니다.");
        return response.success(userService.getUserFollowingList(user.get(), dto));
    }

    @DeleteMapping("/users/follower/{id}")
    public ResponseEntity<?> removeUserFollowingList(@PathVariable String id) {
        Optional<Users> user = userService.getAppUser();
        if (user.isEmpty()) throw new RuntimeException("사용자 정보를 조회할 수 없습니다.");
        userService.removeFollower(user.get(), id);
        return response.success();
    }

    @PostMapping("/users/leave")
    public ResponseEntity<?> leaveUser(@RequestBody LeaveUserRequest dto) {
        userService.leaveRequest(dto);
        return response.success();
    }
}