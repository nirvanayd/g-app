package com.nelly.application.service.user;

import com.nelly.application.domain.AppAuthentication;
import com.nelly.application.domain.Users;
import com.nelly.application.dto.request.SignUpRequest;
import com.nelly.application.dto.TokenInfoDto;
import com.nelly.application.enums.Authority;
import com.nelly.application.enums.RoleType;
import com.nelly.application.service.UserDomainService;
import com.nelly.application.service.AuthService;
import com.nelly.application.util.CacheTemplate;
import com.nelly.application.util.EncryptUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class UserService {

    private final AuthService authService;
    private final UserDomainService userDomainService;
    private final EncryptUtils encryptUtils;
    private final CacheTemplate cacheTemplate;

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
}
