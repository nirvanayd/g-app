package com.nelly.application.service;

import com.nelly.application.domain.UserStyles;
import com.nelly.application.domain.Users;
import com.nelly.application.enums.StyleType;
import com.nelly.application.enums.UserStatus;
import com.nelly.application.repository.AppUserRepository;
import com.nelly.application.repository.UserStylesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AppUserService {

    private final AppUserRepository userRepository;
    private final UserStylesRepository userStylesRepository;

    public Users addUser(Long authId, String loginId, String email, String birth, String phone ) {
        Users user = Users.builder().authId(authId)
                .loginId(loginId)
                .email(email)
                .birth(birth)
                .phone(phone)
                .status(UserStatus.NORMAL)
                .joinedDate(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }



    public void addUserStyle(Users user, List<String> userStyle) {
        List<UserStyles> list = new ArrayList<>();
        Users refUser = userRepository.getById(user.getId());
        for (String code: userStyle) {
            UserStyles userStyles = UserStyles.builder().styleType(StyleType.getStyleType(code)).user(refUser).build();
            userStylesRepository.save(userStyles);
        }
    }

    public Users getUsers(long authId) {
        Users user = userRepository.findByAuthId(authId).orElse(null);
        if (user == null) throw new RuntimeException("사용자를 찾을 수 없습니다.");
        return user;
    }
}
