package com.nelly.application.service;

import com.nelly.application.domain.Users;
import com.nelly.application.dto.TokenInfoDto;
import com.nelly.application.enums.Authority;
import com.nelly.application.jwt.TokenProvider;
import com.nelly.application.repository.UserRepository;
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

    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;

    public Users signUp(String email, String password) {
        Users user = Users.builder()
                .email(email)
                .password(password)
                .roles(Collections.singletonList(Authority.ROLE_USER.name()))
                .build();

        return userRepository.save(user);
    }

    public Users findByEmail(String email) {
        Optional<Users> existUser = userRepository.findByEmail(email);
        return existUser.orElse(null);
    }

    public void login(String email, String password) {
        if (userRepository.findByEmail(email).orElse(null) == null) {
            throw new RuntimeException("아이디를 찾을 수 없습니다.");
        }

        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenInfoDto tokenInfoDto = tokenProvider.generateToken(authentication);

        System.out.println(tokenInfoDto);
        System.out.println("***********");
        System.out.println(authentication.getName());

    }
}
