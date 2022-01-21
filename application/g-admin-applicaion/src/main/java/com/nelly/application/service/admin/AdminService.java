package com.nelly.application.service.admin;

import com.nelly.application.domain.Users;
import com.nelly.application.dto.SignUpRequestDto;
import com.nelly.application.dto.TokenInfoDto;
import com.nelly.application.enums.Authority;
import com.nelly.application.service.AppUserService;
import com.nelly.application.service.AuthService;
import com.nelly.application.util.CacheTemplate;
import com.nelly.application.util.EncryptUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class AdminService {

    private final AuthService authService;
    private final AppUserService appUserService;
    private final EncryptUtils encryptUtils;
    private final CacheTemplate cacheTemplate;

    private static final String BEARER_TYPE = "Bearer";


    public void signUp(SignUpRequestDto dto) {
        if(authService.findByLoginId(dto.getLoginId()) != null) throw new RuntimeException("사용 중인 아이디입니다.");
        // 비밀번호 암호화
        String encryptPassword = encryptUtils.encrypt(dto.getPassword());
        Long authId = authService.signUp(dto.getLoginId(), encryptPassword, Authority.ROLE_ADMIN);
        if (authId == null) throw new RuntimeException("회원가입 중 오류가 발생하였습니다.");
        Users user = appUserService.addUser(authId, dto.getLoginId(), dto.getEmail(), dto.getBirth(), dto.getPhone(),
                Authority.ROLE_ADMIN);

        appUserService.addUserStyle(user, dto.getUserStyle());
    }

    public String login(String loginId, String password) {
        TokenInfoDto tokenInfoDto = authService.login(loginId, password);
        // redis 저장
        cacheTemplate.putValue(String.valueOf(tokenInfoDto.getAuthId()), tokenInfoDto.getRefreshToken(), "token",
                tokenInfoDto.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);
        // return
        return tokenInfoDto.getAccessToken();
    }

    public void authority() {
        // SecurityContext에 담겨 있는 authentication userEamil 정보
        authService.authority();
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

        System.out.println(tokenInfoDto.getRefreshTokenExpirationTime());
        cacheTemplate.putValue(token, "logout", tokenInfoDto.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);
    }

    public Users userTest(String token) {
        TokenInfoDto tokenInfoDto = authService.getAppAuthentication(token);
        long authId = tokenInfoDto.getAuthId();

        return appUserService.getUsers(authId);
    }
}
