package com.nelly.application.service;

import com.nelly.application.domain.AppAuthentication;
import com.nelly.application.dto.TokenInfoDto;
import com.nelly.application.enums.Authority;
import com.nelly.application.exception.AccessDeniedException;
import com.nelly.application.jwt.TokenProvider;
import com.nelly.application.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
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

    public TokenInfoDto login(String loginId, String password, String role) {
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

        auth.getRoles().stream().filter(c -> c.equals(role)).findFirst().orElseThrow(()->new AccessDeniedException("접근 권한이 없습니다."));

        auth.setRt(tokenInfoDto.getRefreshToken());
        authRepository.save(auth);

        tokenInfoDto.setAuthId(auth.getId());
        return tokenInfoDto;
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

    public TokenInfoDto getExistTokenInfo(String accessToken, String refreshToken) {
        if(!tokenProvider.validateToken(refreshToken)) {
            log.info("##### validate fail");
        }
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        AppAuthentication auth = findByLoginId(authentication.getName());

        return TokenInfoDto.builder().authId(auth.getId())
                .refreshToken(auth.getRt())
                .build();
    }

    public TokenInfoDto reissue(String accessToken) {
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        AppAuthentication auth = findByLoginId(authentication.getName());
        TokenInfoDto tokenInfoDto = tokenProvider.generateToken(authentication);
        // authId 갱신
        tokenInfoDto.setAuthId(auth.getId());
        return tokenInfoDto;
    }

    public long getAuthenticationId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppAuthentication auth = findByLoginId(authentication.getName());
        if (auth == null) throw new RuntimeException("회원 정보를 조회할 수 없습니다.");
        return auth.getId();
    }

    public Long getAppAuthenticationId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppAuthentication auth = findByLoginId(authentication.getName());
        if (auth == null) return null;
        return auth.getId();
    }

    public void resetPassword(String loginId, String password) {
        AppAuthentication auth = authRepository.findByLoginId(loginId).orElse(null);
        if (auth == null) {
            throw new RuntimeException("아이디를 찾을 수 없습니다.");
        }
        auth.setPassword(password);
        authRepository.save(auth);
        SecurityContextHolder.clearContext();
    }


}
