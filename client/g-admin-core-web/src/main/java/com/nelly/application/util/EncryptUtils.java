package com.nelly.application.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EncryptUtils {
    private final PasswordEncoder passwordEncoder;
    public String encrypt(String str) {
        return passwordEncoder.encode(str);
    }
}
