package com.nelly.application.dto.response;

import com.nelly.application.domain.UserFollow;
import com.nelly.application.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetUserFollowerResponse {
    private long id;
    private ContentMemberResponse member;

    public List<GetUserFollowerResponse> toDtoList(List<UserFollow> userFollowList) {
        return userFollowList.stream().map(this::toDto).collect(Collectors.toList());
    }

    public GetUserFollowerResponse toDto(UserFollow userFollow) {
        Users user = userFollow.getUser();
        ContentMemberResponse memberResponse = new ContentMemberResponse();
        return GetUserFollowerResponse.builder().
                id(userFollow.getId()).
                member(memberResponse.contentUserToResponse(user)).
                build();
    }
}
