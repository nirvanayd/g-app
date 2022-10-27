package com.nelly.application.dto.response;

import com.nelly.application.domain.ContentLikes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetContentLikeResponse {
    private long id;
    private ContentMemberResponse member;
    private boolean follow;

    public GetContentLikeResponse toDto(ContentLikes contentLikes) {
        ContentMemberResponse contentMemberResponse = new ContentMemberResponse();

        return GetContentLikeResponse.builder().
                member(contentMemberResponse.contentUserToResponse(contentLikes.getUser())).
                id(contentLikes.getId()).build();
    }

    public List<GetContentLikeResponse> toDtoList(List<ContentLikes> contentLikesList) {
        return contentLikesList.stream().map(this::toDto).collect(Collectors.toList());
    }
}