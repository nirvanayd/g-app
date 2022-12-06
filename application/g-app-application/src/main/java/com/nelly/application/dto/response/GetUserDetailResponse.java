package com.nelly.application.dto.response;

import com.nelly.application.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 @override

 //팔로우 여부
 bool followed;

 //회원의 게시물
 List<Contents> contentsList;
 */

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetUserDetailResponse {
    private Long id;
    private String nickname;
    private String photoURL;
    private String backgroundPhotoURL;
    private String profileTitle;
    private String profileText;
    private String status;
    private String description;
    private int followerCount;
    private long likeCount;
    private long markCount;
    private int contentsCount;
    private boolean followed;
    private List<ContentThumbResponse> contentList;

    public GetUserDetailResponse toDto(Users u) {
        return GetUserDetailResponse.builder().
                id(u.getId()).
                nickname(u.getLoginId()).
                photoURL(u.getProfileImageUrl()).
                backgroundPhotoURL(u.getBackgroundImageUrl()).
                profileTitle(u.getProfileTitle()).
                profileText(u.getProfileText()).
                status(u.getStatus().toString()).
                followerCount(u.getFollowingCount()).build();
    }
}
