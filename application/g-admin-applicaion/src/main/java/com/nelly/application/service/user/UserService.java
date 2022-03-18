package com.nelly.application.service.user;

import com.nelly.application.domain.Users;
import com.nelly.application.dto.request.GetAccountListRequest;
import com.nelly.application.enums.RoleType;
import com.nelly.application.enums.UserStatus;
import com.nelly.application.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserDomainService usersDomainService;

    public Page<Users> getAccountList(GetAccountListRequest requestDto) {
        UserStatus status = (requestDto.getStatusCode() == null) ? null : UserStatus.getUserStatus(requestDto.getStatusCode());
        return usersDomainService.selectAccountList(requestDto.getPage(), requestDto.getSize(), requestDto.getLoginId(), requestDto.getEmail(),
                status, RoleType.USER.getCode());
    }

    public Users getAccountDetail(long userId) {
        return usersDomainService.selectAccount(userId);
    }

    public List<Users> getAccountList(String loginId) {
        return usersDomainService.selectAccountListByLoginId(loginId);
    }
}
