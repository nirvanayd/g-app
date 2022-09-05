package com.nelly.application.dto.response;

import com.nelly.application.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentMemberResponse {
    private Long id;
    private String nickname;
    private String photoURL;

    public ContentMemberResponse contentUserToResponse(Users user) {
        return ContentMemberResponse.builder().
                id(user.getId()).
                nickname(user.getLoginId()).
                photoURL(user.getProfileImageUrl()).
                build();
    }
}
