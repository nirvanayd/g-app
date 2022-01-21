package com.nelly.application.service;

import com.nelly.application.domain.AppAuthentication;
import com.nelly.application.dto.AuthTokenInfoDto;
import com.nelly.application.dto.TokenInfoDto;
import com.nelly.application.enums.Authority;
import com.nelly.application.jwt.TokenProvider;
import com.nelly.application.repository.AuthRepository;
import com.nelly.application.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthRepository authRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;

    public Long signUp(String loginId, String password) {
        AppAuthentication authentication = AppAuthentication.builder()
                .loginId(loginId)
                .password(password)
                .roles(Collections.singletonList(Authority.ROLE_USER.name()))
                .build();

        return authRepository.save(authentication).getId();
    }

    public Long signUp(String loginId, String password, Authority authority) {
        AppAuthentication authentication = AppAuthentication.builder()
                .loginId(loginId)
                .password(password)
                .roles(Collections.singletonList(authority.name()))
                .build();

        return authRepository.save(authentication).getId();
    }

    public AppAuthentication findByLoginId(String loginId) {
        Optional<AppAuthentication> existUser = authRepository.findByLoginId(loginId);
        return existUser.orElse(null);
    }

    public TokenInfoDto login(String loginId, String password) {
        if (authRepository.findByLoginId(loginId).orElse(null) == null) {
            throw new RuntimeException("아이디를 찾을 수 없습니다.");
        }
        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginId, password);

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenInfoDto tokenInfoDto = tokenProvider.generateToken(authentication);

        // 3. refresh 토큰 DB 업데이트.
        AppAuthentication auth = authRepository.findByLoginId(loginId).orElse(null);
        if (auth == null) {
            throw new RuntimeException("아이디를 찾을 수 없습니다.");
        }
        auth.setRt(tokenInfoDto.getRefreshToken());
        authRepository.save(auth);

        tokenInfoDto.setAuthId(auth.getId());
        return tokenInfoDto;
    }

    public void authority() {
        String userName = SecurityUtil.getCurrentUser();
        System.out.println("#####" + userName);

//        Users user = usersRepository.findByEmail(userEmail)
//                .orElseThrow(() -> new UsernameNotFoundException("No authentication information."));
//
//        // add ROLE_ADMIN
//        user.getRoles().add(Authority.ROLE_ADMIN.name());
//        usersRepository.save(user);
//
//        return response.success();
    }

    public TokenInfoDto getAppAuthentication(String token) {
        if(!tokenProvider.validateToken(token)) {
            return null;
        }
        Authentication authentication = tokenProvider.getAuthentication(token);
        AppAuthentication auth = findByLoginId(authentication.getName());

        Long expire = tokenProvider.getExpiration(token);
        return TokenInfoDto.builder().authId(auth.getId())
                .loginId(auth.getLoginId())
                .accessToken(token)
                .refreshToken(auth.getRt())
                .refreshTokenExpirationTime(expire)
                .build();
    }
}
