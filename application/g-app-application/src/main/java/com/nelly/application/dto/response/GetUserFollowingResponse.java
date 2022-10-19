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
public class GetUserFollowingResponse {
    private long id;
    private ContentMemberResponse member;

    public List<GetUserFollowingResponse> toDtoList(List<UserFollow> userFollowList) {
        return userFollowList.stream().map(this::toDto).collect(Collectors.toList());
    }

    public GetUserFollowingResponse toDto(UserFollow userFollow) {
        Users user = userFollow.getFollower();
        ContentMemberResponse memberResponse = new ContentMemberResponse();
        return GetUserFollowingResponse.builder().
                id(userFollow.getId()).
                member(memberResponse.contentUserToResponse(user)).
                build();
    }
}
