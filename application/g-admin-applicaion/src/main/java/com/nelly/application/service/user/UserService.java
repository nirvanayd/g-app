package com.nelly.application.service.user;

import com.nelly.application.service.AppUserService;
import com.nelly.application.service.AuthService;
import com.nelly.application.util.CacheTemplate;
import com.nelly.application.util.EncryptUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final AppUserService appUserService;
    private final CacheTemplate cacheTemplate;

    public void getUsers() {

    }
}
