package com.nelly.application.service.user;

import com.nelly.application.service.AuthService;
import com.nelly.application.util.EncryptUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final AuthService authService;
    private final EncryptUtils encryptUtils;

    public void signUp(String email, String password) {
        if(authService.findByEmail(email) != null) throw new RuntimeException("사용 중인 이메일입니다.");
        // 비밀번호 암호화
        String encryptPassword = encryptUtils.encrypt(password);
        authService.signUp(email, encryptPassword);
    }

    public void login(String email, String password) {
        authService.login(email, password);
    }
}
