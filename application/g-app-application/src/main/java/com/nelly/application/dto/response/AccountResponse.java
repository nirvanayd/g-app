package com.nelly.application.dto.response;

import com.nelly.application.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
    private Long id;
    private String nickname;
    private String photoURL;

    public AccountResponse toDto(Users user) {
        return AccountResponse.builder().
                id(user.getId()).
                nickname(user.getLoginId()).
                photoURL(user.getProfileImageUrl()).
                build();
    }

    public List<AccountResponse> toDtoList(List<Users> userList) {
        return userList.stream().map(this::toDto).collect(Collectors.toList());
    }
}