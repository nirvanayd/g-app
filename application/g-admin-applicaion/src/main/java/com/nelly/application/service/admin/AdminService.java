package com.nelly.application.service.admin;


import com.nelly.application.domain.Users;
import com.nelly.application.dto.SignUpRequestDto;
import com.nelly.application.dto.TokenInfoDto;
import com.nelly.application.dto.request.ReissueRequest;
import com.nelly.application.enums.Authority;
import com.nelly.application.service.AppUserService;
import com.nelly.application.service.AuthService;
import com.nelly.application.util.CacheTemplate;
import com.nelly.application.util.EncryptUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
@Slf4j
public class AdminService {

    private final AuthService authService;
    private final AppUserService appUserService;
    private final EncryptUtils encryptUtils;
    private final CacheTemplate cacheTemplate;

    private static final String BEARER_TYPE = "Bearer";


    @Transactional
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

    public TokenInfoDto login(String loginId, String password) {
        TokenInfoDto tokenInfoDto = authService.login(loginId, password);
        // redis 저장
        cacheTemplate.putValue(String.valueOf(tokenInfoDto.getAuthId()), tokenInfoDto.getRefreshToken(), "token",
                tokenInfoDto.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);
        // return
        return tokenInfoDto;
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

        return appUserService.getUsers(authId);
    }

    public TokenInfoDto reissue(ReissueRequest requestDto) {
        TokenInfoDto tokenInfoDto = authService.getExistTokenInfo(requestDto.getAccessToken(), requestDto.getRefreshToken());

        // token 값 취득
        String cacheToken = cacheTemplate.getValue(String.valueOf(tokenInfoDto.getAuthId()), "token");
        log.info("### : " + cacheToken);
        log.info("@@@ : " + requestDto.getRefreshToken());
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
}
