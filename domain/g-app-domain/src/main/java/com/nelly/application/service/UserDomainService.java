package com.nelly.application.service;

import com.nelly.application.domain.UserStyles;
import com.nelly.application.domain.Users;
import com.nelly.application.enums.Authority;
import com.nelly.application.enums.StyleType;
import com.nelly.application.enums.UserStatus;
import com.nelly.application.repository.AppUserRepository;
import com.nelly.application.repository.UserStylesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserDomainService {

    private final AppUserRepository userRepository;
    private final UserStylesRepository userStylesRepository;

    public Users addUser(Long authId, String loginId, String email, String birth, String phone, Authority authority) {
        Users user = Users.builder().authId(authId)
                .loginId(loginId)
                .role(authority.name())
                .email(email)
                .birth(birth)
                .phone(phone)
                .status(UserStatus.NORMAL)
                .joinedDate(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }



    public void addUserStyle(Users user, List<String> userStyle) {
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

    /* admin 사용 */
    public Page<Users> selectAccountList(Integer page, Integer size, String role) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        return userRepository.findAllByRole(role, pageRequest);
    }

    public Page<Users> selectAccountList(Integer page, Integer size, String email, UserStatus status, String role) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        if (email == null) return userRepository.findAllByStatusAndRole(status, role, pageRequest);
        if (status == null) return userRepository.findAllByEmailContainsAndRole(email, role, pageRequest);
        return userRepository.findAllByEmailContainsAndStatusAndRole(email, status, role, pageRequest);
    }

    public Page<Users> selectAccountList(Integer page, Integer size, String loginId, String email, UserStatus status, String role) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        if (loginId == null && email == null && status == null) return selectAccountList(page, size, role);
        if (loginId == null) return selectAccountList(page, size, email, status, role);
        if (email == null && status == null) {
            return userRepository.findAllByLoginIdContainsAndRole(loginId, role, pageRequest);
        }
        if (status == null) return userRepository.findAllByLoginIdContainsAndEmailContainsAndRole(loginId, email, role, pageRequest);
        if (email == null) return userRepository.findAllByLoginIdContainsAndStatusAndRole(loginId, status, role, pageRequest);
        return userRepository.findAllByLoginIdContainsAndEmailContainsAndStatusAndRole(loginId, email, status, role, pageRequest);
    }

    public Users selectAccount(long userId) {
        Users user = userRepository.findById(userId).orElse(null);
        if (user == null) throw new RuntimeException("사용자를 찾을 수 없습니다.");
        return user;
    }
}
