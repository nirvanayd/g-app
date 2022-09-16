package com.nelly.application.dto.response;

import com.nelly.application.domain.ContentLikes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetContentLikeResponse {
    private long id;
    private ContentMemberResponse member;
    private boolean follow;

//    public GetContentLikeResponse toDto(ContentLikes contentLikes) {
//        ContentMemberResponse memberResponse = new ContentMemberResponse();
//
//    }
}