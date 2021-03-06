package com.nelly.application.service.user;

import com.nelly.application.domain.Users;
import com.nelly.application.dto.request.SignUpRequest;
import com.nelly.application.dto.TokenInfoDto;
import com.nelly.application.enums.Authority;
import com.nelly.application.enums.RoleType;
import com.nelly.application.mail.MailSender;
import com.nelly.application.service.UserDomainService;
import com.nelly.application.service.AuthService;
import com.nelly.application.util.CacheTemplate;
import com.nelly.application.util.EncryptUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class UserService {

    private final AuthService authService;
    private final UserDomainService userDomainService;
    private final EncryptUtils encryptUtils;
    private final CacheTemplate cacheTemplate;
    private final MailSender mailSender;

    private static final String BEARER_TYPE = "Bearer";


    public void signUp(SignUpRequest dto) {
        if(authService.findByLoginId(dto.getLoginId()) != null) throw new RuntimeException("사용 중인 아이디입니다.");
        // 비밀번호 암호화
        String encryptPassword = encryptUtils.encrypt(dto.getPassword());
        Long authId = authService.signUp(dto.getLoginId(), encryptPassword);
        if (authId == null) throw new RuntimeException("회원가입 중 오류가 발생하였습니다.");
        Users user = userDomainService.addUser(authId, dto.getLoginId(), dto.getEmail(), dto.getBirth(),
                Authority.ROLE_USER);

        userDomainService.addUserStyle(user, dto.getUserStyle());
    }

    public String login(String loginId, String password) {
        TokenInfoDto tokenInfoDto = authService.login(loginId, password, RoleType.USER.getCode());
        // redis 저장
        cacheTemplate.putValue(String.valueOf(tokenInfoDto.getAuthId()), tokenInfoDto.getRefreshToken(), "token",
                tokenInfoDto.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);
        // return
        return tokenInfoDto.getAccessToken();
    }

    public String getToken(String bearerToken) {
        if (bearerToken == null) return null;
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public void logout(String token) {
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
}
