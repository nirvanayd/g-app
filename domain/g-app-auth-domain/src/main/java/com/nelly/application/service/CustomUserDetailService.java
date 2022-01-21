package com.nelly.application.service;

import com.nelly.application.domain.AppAuthentication;
import com.nelly.application.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.SQLSyntaxErrorException;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final AuthRepository authRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        System.out.println("@#@# : " + loginId);
        return authRepository.findByLoginId(loginId)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 정보를 조회할 수 없습니다."));
    }

    private UserDetails createUserDetails(AppAuthentication authentication) {

        return new User(authentication.getUsername(), authentication.getPassword(), authentication.getAuthorities());
    }
}
