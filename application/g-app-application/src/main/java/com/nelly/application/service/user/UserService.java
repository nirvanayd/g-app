package com.nelly.application.service.user;

import com.nelly.application.config.AwsProperties;
import com.nelly.application.domain.*;
import com.nelly.application.dto.request.*;
import com.nelly.application.dto.TokenInfoDto;
import com.nelly.application.dto.response.*;
import com.nelly.application.enums.*;
import com.nelly.application.exception.AuthenticationException;
import com.nelly.application.exception.NoContentException;
import com.nelly.application.exception.SystemException;
import com.nelly.application.mail.MailSender;
import com.nelly.application.repository.UserNotificationTokensRepository;
import com.nelly.application.service.ContentDomainService;
import com.nelly.application.service.ScraperDomainService;
import com.nelly.application.service.UserDomainService;
import com.nelly.application.service.AuthService;
import com.nelly.application.util.AgeUtil;
import com.nelly.application.util.CacheTemplate;
import com.nelly.application.util.EncryptUtils;
import com.nelly.application.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final AuthService authService;
    private final UserDomainService userDomainService;
    private final ContentDomainService contentDomainService;
    private final ScraperDomainService scraperDomainService;
    private final EncryptUtils encryptUtils;
    private final CacheTemplate cacheTemplate;
    private final MailSender mailSender;
    private final AwsProperties awsProperties;
    private final S3Uploader s3Uploader;
    private final ModelMapper modelMapper;

    private static final String BEARER_TYPE = "Bearer";
    private static final String DIRECTORY_SEPARATOR = "/";

    @Transactional
    public void signUp(SignUpRequest dto) {
        if (authService.findByLoginId(dto.getLoginId()) != null) throw new RuntimeException("사용 중인 아이디입니다.");
        if (userDomainService.existEmail(dto.getEmail())) throw new RuntimeException("사용 중인 이메일입니다.");
        // 14세 확인
        int age = AgeUtil.getAge(AgeUtil.getYear(dto.getBirth()), AgeUtil.getMonth(dto.getBirth()),
                AgeUtil.getDate(dto.getBirth()));
        if (age < 14) {
            throw new SystemException("만 14세 미만은 가입할 수 없습니다.");
        }
        // 비밀번호 암호화
        String encryptPassword = encryptUtils.encrypt(dto.getPassword());
        // 마케팅 수신동의
        Long authId = authService.signUp(dto.getLoginId(), encryptPassword);
        if (authId == null) throw new RuntimeException("회원가입 중 오류가 발생하였습니다.");
        Users user = userDomainService.addUser(authId, dto.getLoginId(), dto.getEmail(), dto.getBirth(), Authority.ROLE_USER);

        addUserAgreement(user, dto.getAgreementList());

        userDomainService.addUserStyle(user, dto.getUserStyle());
        userDomainService.addUserMarketingType(user, dto.getUserMarketingType());
    }

    @Transactional
    public TokenInfoDto login(LoginRequest request) {

        String loginId = request.getLoginId();
        String password = request.getPassword();

        TokenInfoDto tokenInfoDto = authService.login(loginId, password, RoleType.USER.getCode());
        if (request.getFcmToken() != null) {
            Optional<Users> users = userDomainService.selectAppUsers(tokenInfoDto.getAuthId());
            users.ifPresent(value -> createUserFcmToken(value, request.getFcmToken()));
        }

        removeUserToken(tokenInfoDto.getAuthId());
        // 해당 authId의 토큰은 로그아웃처리.
//        String existToken = cacheTemplate.getValue(String.valueOf(tokenInfoDto.getAuthId()), "accessToken");
//        if (existToken != null) {
//            try {
//                Long expireTime = authService.getExpiration(existToken);
//                cacheTemplate.putValue(existToken, "logout", expireTime, TimeUnit.MILLISECONDS);
//            } catch (Exception e) {}
//        }

        // redis 저장
        cacheTemplate.putValue(String.valueOf(tokenInfoDto.getAuthId()), tokenInfoDto.getRefreshToken(), "token",
                tokenInfoDto.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        cacheTemplate.putValue(String.valueOf(tokenInfoDto.getAuthId()), tokenInfoDto.getAccessToken(), "accessToken",
                tokenInfoDto.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        // return
        return tokenInfoDto;
    }

    public void removeUserToken(long authId) {
        String existToken = cacheTemplate.getValue(String.valueOf(authId), "accessToken");
        log.info(existToken);
        if (existToken != null) {
            try {
                Long expireTime = authService.getExpiration(existToken);
                cacheTemplate.putValue(existToken, "logout", expireTime, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getToken(String bearerToken) {
        if (bearerToken == null) return null;
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public void logout(Users user, String token) {
        // fcm 토큰 삭제처리
        removeUserFcmToken(user);
        TokenInfoDto tokenInfoDto = authService.getAppAuthentication(token);
        if (cacheTemplate.getValue(String.valueOf(tokenInfoDto.getAuthId()), "token") != null) {
            cacheTemplate.deleteCache(String.valueOf(tokenInfoDto.getAuthId()), "token");
        }
        cacheTemplate.putValue(token, "logout", tokenInfoDto.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);
    }

    public Users userTest(String token) {
        TokenInfoDto tokenInfoDto = authService.getAppAuthentication(token);
        long authId = tokenInfoDto.getAuthId();
        return userDomainService.getUsers(authId);
    }

    public Users getUser() {
        // get user default
        long authId = authService.getAuthenticationId();
        // 회원정보 조회
        // 추후 캐시로 전환
        return userDomainService.getUsers(authId);
    }

    /**
     * Optional<Users>
     * @return
     */
    public Optional<Users> getAppUser() {
        Long authId = authService.getAppAuthenticationId();
        if (authId == null) return Optional.empty();
        return userDomainService.selectAppUsers(authId);
    }

    public Optional<Users> getAppUser(long authId) {
        return userDomainService.selectAppUsers(authId);
    }

    public Users getUser(long userId) {
        return userDomainService.selectAccount(userId);
    }

    public void getUserDefault() {
        // redis에서 정보 조회
        long authId = authService.getAuthenticationId();
        cacheTemplate.getValue(String.valueOf(authId), "user");
    }

    public Optional<Users> existId(String loginId) {
        return userDomainService.selectAccountByLoginId(loginId);
    }

    public Optional<Users> existEmail(String email) {
        return userDomainService.selectAccountByEmail(email);
    }

    public void findId(String email) {
        // 이메일로 계정이 존재하는지 확인
        Optional<Users> user = existEmail(email);
        if (user.isEmpty()) {
            throw new RuntimeException("등록된 이메일이 없습니다.");
        }

        String content = "회원님의 아이디는 <strong>" + user.get().getLoginId() + "</strong> 입니다.";
        String subject = "Filunaway 아이디 찾기 결과";
        mailSender.sendMail(subject, content, email);
    }

    public void resetPassword(String email) {
        Users user = existEmail(email).orElse(null);
        if (user == null) {
            throw new RuntimeException("등록된 이메일이 없습니다.");
        }

        // random 문자열 생성
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 8;
        Random random = new Random();

        String generatedString = random.ints(leftLimit,rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        String encryptPassword = encryptUtils.encrypt(generatedString);

        authService.resetPassword(user.getLoginId(), encryptPassword);

        String content = "회원님의 비밀번호는 <strong>" + generatedString + "</strong> 입니다.";
        String subject = "Filunaway 비밀번호 초기화 결과 =";
        mailSender.sendMail(subject, content, email);
    }

    public TokenInfoDto reissue(ReissueRequest requestDto) {
        TokenInfoDto tokenInfoDto = authService.getExistTokenInfo(requestDto.getAccessToken(), requestDto.getRefreshToken());

        // token 값 취득
        String cacheToken = cacheTemplate.getValue(String.valueOf(tokenInfoDto.getAuthId()), "token");
        if (!cacheToken.equals(requestDto.getRefreshToken())) {
            throw new RuntimeException("토큰정보가 일치하지 않습니다.");
        }
        if (ObjectUtils.isEmpty(cacheToken)) {
            throw new RuntimeException("잘못된 요청입니다.");
        }

        TokenInfoDto newTokenInfoDto = authService.reissue(requestDto.getAccessToken());
        cacheTemplate.putValue(String.valueOf(newTokenInfoDto.getAuthId()), newTokenInfoDto.getRefreshToken(), "token",
                newTokenInfoDto.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);
        return newTokenInfoDto;
    }

    public void updatePassword(Users user, UpdatePasswordRequest dto) {
        authService.checkAppUserPassword(user.getLoginId(), dto.getOldPassword(), RoleType.USER.getCode());

        String encryptPassword = encryptUtils.encrypt(dto.getNewPassword());
        authService.resetPassword(user.getLoginId(), encryptPassword);
    }

    public void updateEmail(Users user, UpdateEmailRequest dto) {
        // 기존 이메일 일치여부
        if (!user.getEmail().equals(dto.getOldEmail())) {
            throw new RuntimeException("기존 이메일 정보가 일치하지 않습니다.");
        }
        if (userDomainService.existEmail(dto.getNewEmail())) throw new RuntimeException("사용 중인 이메일입니다.");
        userDomainService.saveAccountEmail(user, dto.getNewEmail());
    }

    public void updateAgreement(Users user, UpdateAgreementRequest dto) {
        userDomainService.saveUserAgreement(user.getId(), dto.getAgreementType(), dto.getUseYn());
    }

    @Transactional
    public void updateMarketingAgreement(Users user, UpdateMarketingAgreementRequest dto) {
        userDomainService.saveUserAgreement(user.getId(), AgreementType.MARKETING_POLICY.getCode(), dto.getUseYn());
        userDomainService.addUserMarketingType(user, dto.getUserMarketingType());
    }

    public List<UserAgreements> getAppUserAgreements(Users user) {
        return userDomainService.getUserAgreements(user);
    }

    public List<UserMarketing> getUserMarketingList(Users user) {
        return userDomainService.selectUserMarketingList(user);
    }

    public void addUserAgreement(Users user, List<UserAgreementRequest> list) {
        list.forEach(l -> userDomainService.addUserAgreement(user, l.getAgreementType(), l.getValue()));
    }

    public UserNotificationTokens createUserFcmToken(Users user, String fcmToken) {
        if (fcmToken == null || fcmToken.isEmpty()) return null;
        // exist check
        Optional<UserNotificationTokens> userTokens = userDomainService.existFcmToken(user);

        // insert or update
        if (userTokens.isPresent()) {
            return userDomainService.saveUserToken(userTokens.get(), fcmToken);
        }
        return userDomainService.saveUserToken(user, fcmToken);
    }

    public void removeUserFcmToken(Users user) {
        Optional<UserNotificationTokens> userTokens = userDomainService.existFcmToken(user);
        if (userTokens.isEmpty()) return;
        userDomainService.deleteUserFcmToken(userTokens.get());
    }

    public void updateUserStyle(Users user, UpdateUserStyleRequest dto) {
        List<String> userStyleList = dto.getUserStyle();
        userDomainService.saveUserStyles(user, userStyleList);
    }


    public void saveUserFollow(Users user, SaveFollowRequest dto) {
        Long followingId = Long.parseLong(dto.getUserId());
        Users followingUser = userDomainService.selectAccount(followingId);

        Optional<UserFollow> selectUserFollow = userDomainService.selectUserFollow(user, followingUser);

        if (dto.getFollowYn().equals(YesOrNoType.YES.getCode())) {
            if (selectUserFollow.isEmpty()) {
                userDomainService.saveUserFollow(user, followingUser);
                cacheTemplate.incrValue(String.valueOf(user.getId()), "follower");
                cacheTemplate.incrValue(String.valueOf(followingUser.getId()), "following");
            }
        } else if (dto.getFollowYn().equals(YesOrNoType.NO.getCode())) {
            if (selectUserFollow.isPresent()) {
                UserFollow userFollow = selectUserFollow.get();
                userDomainService.deleteUserFollow(userFollow.getId());
                cacheTemplate.decrValue(String.valueOf(user.getId()), "follower");
                cacheTemplate.decrValue(String.valueOf(followingUser.getId()), "following");
            }
        }
    }

    public void scheduleFollow() {
        Set<String> followerKeys = cacheTemplate.getKeys("follower");
        for (String key : followerKeys) {
            int value = Integer.parseInt(cacheTemplate.getValue(key));
            Long userId = Long.parseLong(cacheTemplate.parseCashNameKey(key).get("key"));
            userDomainService.updateUserFollowerCount(userId, value);
            cacheTemplate.deleteCache(key);
        }

        Set<String> followingKeys = cacheTemplate.getKeys("following");
        for (String key : followingKeys) {
            int value = Integer.parseInt(cacheTemplate.getValue(key));
            Long userId = Long.parseLong(cacheTemplate.parseCashNameKey(key).get("key"));
            userDomainService.updateUserFollowingCount(userId, value);
            cacheTemplate.deleteCache(key);
        }
    }

    public GetUserDetailResponse getUserDetail(Long userDetailId, Optional<Users> user) {
        Users detailUser = getUser(userDetailId);
        GetUserDetailResponse getUserDetailResponse = new GetUserDetailResponse();
        GetUserDetailResponse response = getUserDetailResponse.toDto(detailUser);
        int page = 0;
        int size = 9;

        Page<Contents> selectContentList = contentDomainService.selectContentList(detailUser, page, size);

        long userLikeCount = contentDomainService.countUserLike(detailUser);
        long userMarkCount = contentDomainService.countUserMark(detailUser);

        List<ContentThumbResponse> list = new ArrayList<>();
        long totalContentCount = selectContentList.getTotalElements();
        ContentThumbResponse contentThumbResponse = new ContentThumbResponse();
        List<ContentThumbResponse> contentList =
                contentThumbResponse.toDtoList(selectContentList.getContent());

        // 팔로우 유무
        if (user.isPresent()) {
            Optional<UserFollow> selectUserFollow = userDomainService.selectUserFollow(user.get(), detailUser);
            if (selectUserFollow.isPresent()) {
                response.setFollowed(true);
            }
        }

        response.setContentsCount((int)totalContentCount);
        response.setLikeCount(userLikeCount);
        response.setMarkCount(userMarkCount);
        response.setContentList(contentList);
        return response;
    }

    public GetMyPageResponse getUserDetailOwner(Long userDetailId) {
        Users ownerUser = getUser(userDetailId);
        Optional<Users> appUser = getAppUser();
        if (appUser.isEmpty()) throw new AuthenticationException();
        if (!Objects.equals(appUser.get().getId(), userDetailId)) throw new SystemException("접근 권한이 없습니다.");

        GetMyPageResponse getMyPageResponse = new GetMyPageResponse();
        GetMyPageResponse response = getMyPageResponse.toDto(ownerUser);
        int page = 0;
        int contentSize = 9;
        Page<Contents> selectContentList = contentDomainService.selectOwnerContentList(ownerUser, page, contentSize);
        Page<ContentMarks> selectMarkList = contentDomainService.selectUserMarkList(ownerUser, page, contentSize);
        Page<UserScrapCart> selectUserCart = scraperDomainService.selectUserScrapCartList(ownerUser, page, contentSize);

        long userLikeCount = contentDomainService.countUserLike(ownerUser);
        long userMarkCount = contentDomainService.countUserMark(ownerUser);

        List<ContentThumbResponse> list = new ArrayList<>();
        long totalContentCount = selectContentList.getTotalElements();
        long totalContentMarkCount = selectMarkList.getTotalElements();
        long totalCartCount = selectUserCart.getTotalElements();
        ContentThumbResponse contentThumbResponse = new ContentThumbResponse();
        MarkContentThumbResponse markContentThumbResponse = new MarkContentThumbResponse();
        ScrapItemResponse scrapItemResponse = new ScrapItemResponse();
        List<ContentThumbResponse> contentList =
                contentThumbResponse.toDtoList(selectContentList.getContent());
        List<MarkContentThumbResponse> markList =
                markContentThumbResponse.toDtoMarkList(selectMarkList.getContent());
        List<ScrapItemResponse> cartList = scrapItemResponse.cartToDtoList(selectUserCart.getContent());

        List<UserAgreements> agreementList = userDomainService.getUserAgreements(ownerUser);
        List<UserAgreementsResponse> userAgreementList =
                agreementList.stream().map(l -> modelMapper.map(l, UserAgreementsResponse.class)).collect(Collectors.toList());

        response.setUserAgreementList(userAgreementList);
        response.setLikeCount(userLikeCount);
        response.setMarkCount(userMarkCount);
        response.setContentsCount((int)totalContentCount);
        response.setContentMarkCount((int)totalContentMarkCount);
        response.setCartCount((int)totalCartCount);
        response.setContentList(contentList);
        response.setContentMarkList(markList);
        response.setCartList(cartList);
        response.setOwner(true);
        return response;
    }

    public List<ContentThumbResponse> getOwnerUserDetailContentList(GetContentListRequest dto) {
        Optional<Users> appUser = getAppUser();
        if (appUser.isEmpty()) throw new SystemException("사용자 정보를 조회할 수 없습니다.");
        Page<Contents> selectContentList = contentDomainService.selectOwnerContentList(appUser.get(), dto.getPage(), dto.getSize());
        ContentThumbResponse contentThumbResponse = new ContentThumbResponse();
        if (selectContentList.isEmpty()) throw new NoContentException();
        return contentThumbResponse.toDtoList(selectContentList.getContent());
    }

    public List<ContentThumbResponse> getUserDetailContentList(Long userDetailId, GetContentListRequest dto) {
        Users detailUser = getUser(userDetailId);
        Optional<Users> appUser = getAppUser();
        dto.setSize(9);
        Page<Contents> selectContentList = contentDomainService.selectContentList(detailUser, dto.getPage(), dto.getSize());
        ContentThumbResponse contentThumbResponse = new ContentThumbResponse();
        if (selectContentList.isEmpty()) throw new NoContentException();
        return contentThumbResponse.toDtoList(selectContentList.getContent());
    }

    public List<MarkContentThumbResponse> getOwnerUserDetailMarkContentList(GetContentListRequest dto) {
        Optional<Users> appUser = getAppUser();
        if (appUser.isEmpty()) throw new SystemException("사용자 정보를 조회할 수 없습니다.");
        dto.setSize(9);
        Page<ContentMarks> selectMarkList = contentDomainService.selectUserMarkList(appUser.get(), dto.getPage(), dto.getSize());
        MarkContentThumbResponse contentThumbResponse = new MarkContentThumbResponse();
        if (selectMarkList.isEmpty()) throw new NoContentException();
        return contentThumbResponse.toDtoMarkList(selectMarkList.getContent());
    }

    public List<MarkContentThumbResponse> getUserDetailMarkContentList(Long userDetailId, GetContentListRequest dto) {
        Users detailUser = getUser(userDetailId);
        dto.setSize(9);
        Page<ContentMarks> selectMarkList = contentDomainService.selectUserMarkList(detailUser, dto.getPage(), dto.getSize());
        MarkContentThumbResponse contentThumbResponse = new MarkContentThumbResponse();
        if (selectMarkList.isEmpty()) throw new NoContentException();
        return contentThumbResponse.toDtoMarkList(selectMarkList.getContent());
    }

    public void getMyPageCartList() {

    }

    @Transactional
    public ImageResponse uploadUserProfileImage(List<MultipartFile> images) throws IOException {
        if (images.size() != 1 ) throw new SystemException("첨부파일 양식이 올바르지 않습니다.");
        // get user id
        Users user = getUser();
        String imageUrl = null;
        for (MultipartFile file: images) {
            imageUrl = awsProperties.getCloudFront().getUrl() +
                    s3Uploader.upload(
                            awsProperties.getS3().getBucket(),
                            file,
                            getS3ProfilePath() + DIRECTORY_SEPARATOR + user.getId());
        }

        if (imageUrl == null) throw new SystemException("이미지 업로드 중 오류가 발생했습니다.");

        return ImageResponse.builder()
                .url(imageUrl)
                .build();
    }

    public String getS3ProfilePath() {
        return awsProperties.getCloudFront().getProfileDir();
    }

    public void saveUserProfileTitle(SaveProfileTitleRequest dto) {
        Users user = getUser();
        userDomainService.saveAccountProfileTitle(user, dto.getProfileTitle());
    }

    public void saveUserProfileText(SaveProfileTextRequest dto) {
        Users user = getUser();
        userDomainService.saveAccountProfileText(user, dto.getProfileText());
    }

    public void saveUserProfileImage(SaveProfileImageRequest dto) {
        Users user = getUser();
        userDomainService.saveAccountProfileImage(user, dto.getImageUrl());
    }

    public void saveUserBackgroundImage(SaveBackgroundImageRequest dto) {
        Users user = getUser();
        userDomainService.saveAccountBackgroundImage(user, dto.getImageUrl());
    }

    public List<GetUserFollowerResponse> getUserFollowerList(Users user, GetFollowerListRequest dto) {
        if (dto.getKeyword() != null && !dto.getKeyword().isEmpty() && !dto.getKeyword().trim().isEmpty()) {
            Page<UserFollow> selectFollowerList = userDomainService.selectAccountFollowerList(user, dto.getKeyword(), dto.getPage(), dto.getSize());
            if (selectFollowerList.isEmpty()) throw new NoContentException();
            GetUserFollowerResponse response = new GetUserFollowerResponse();
            return response.toDtoList(selectFollowerList.getContent());
        }
        Page<UserFollow> selectFollowerList = userDomainService.selectAccountFollowerList(user, dto.getPage(), dto.getSize());
        if (selectFollowerList.isEmpty()) throw new NoContentException();
        GetUserFollowerResponse response = new GetUserFollowerResponse();
        return response.toDtoList(selectFollowerList.getContent());
    }

    public List<GetUserFollowingResponse> getUserFollowingList(Users user, GetFollowingListRequest dto) {
        if (dto.getKeyword() != null && !dto.getKeyword().isEmpty() && !dto.getKeyword().trim().isEmpty()) {
            Page<UserFollow> selectFollowingList = userDomainService.selectAccountFollowingList(user, dto.getKeyword(), dto.getPage(), dto.getSize());
            if (selectFollowingList.isEmpty()) throw new NoContentException();
            GetUserFollowingResponse response = new GetUserFollowingResponse();
            return response.toDtoList(selectFollowingList.getContent());
        }
        Page<UserFollow> selectFollowingList = userDomainService.selectAccountFollowingList(user, dto.getPage(), dto.getSize());
        if (selectFollowingList.isEmpty()) throw new NoContentException();
        GetUserFollowingResponse response = new GetUserFollowingResponse();
        return response.toDtoList(selectFollowingList.getContent());
    }

    public void removeFollower(Users user, String targetId) {
        long followingId = Long.parseLong(targetId);
        Users followingUser = userDomainService.selectAccount(followingId);
        Optional<UserFollow> selectUserFollow = userDomainService.selectUserFollow(followingUser, user);
        if (selectUserFollow.isEmpty()) throw new RuntimeException("팔로워 정보를 조회할 수 없습니다.");
        UserFollow userFollow = selectUserFollow.get();
        userDomainService.deleteUserFollow(userFollow.getId());
        cacheTemplate.decrValue(String.valueOf(user.getId()), "follower");
        cacheTemplate.decrValue(String.valueOf(followingUser.getId()), "following");
    }

    public void checkAuthAppUser(Users user) {
        if (user == null) return;
        if (user.getStatus().equals(UserStatus.BLOCK)) throw new SystemException("사용이 중지된 계정입니다. 관리자에 문의해 주세요.");
        if (user.getStatus().equals(UserStatus.LEAVE)) throw new SystemException("탈퇴 처리된 계정입니다.");
        return;
    }

    public void leaveRequest(LeaveUserRequest dto) {
        Optional<Users> selectedUser = getAppUser();
        if (selectedUser.isEmpty()) throw new SystemException("사용자 정보를 조회할 수 없습니다.");
        Users user = selectedUser.get();
        user.setStatus(UserStatus.LEAVE);
        user.setLeaveDate(LocalDateTime.now());
        // token remove
        removeUserToken(user.getAuthId());
        userDomainService.saveUser(user);
    }

    /**
     * test function
     */
    public void resetBlock() {
        List<Users> blockUserList = userDomainService.selectBlockUserList();
        List<Users> leaveUserList = userDomainService.selectLeaveUserList();

        blockUserList.forEach(l -> {
            l.setStatus(UserStatus.NORMAL);
            userDomainService.saveUser(l);
        });

        leaveUserList.forEach(l -> {
            l.setStatus(UserStatus.NORMAL);
            userDomainService.saveUser(l);
        });
    }
}