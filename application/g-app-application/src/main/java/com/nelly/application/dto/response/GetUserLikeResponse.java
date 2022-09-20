package com.nelly.application.dto.response;

import com.nelly.application.domain.ContentLikes;
import com.nelly.application.dto.request.PageRequest;
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
public class GetUserLikeResponse {
    private long id;
    private String createdAt;
    private ContentMemberResponse member;
    private ContentThumbResponse content;

    public List<GetUserLikeResponse> toDtoList(List<ContentLikes> contentLikeList) {
        return contentLikeList.stream().map(this::toDto).collect(Collectors.toList());
    }

    public GetUserLikeResponse toDto(ContentLikes contentLikes) {
        ContentMemberResponse contentMemberResponse = new ContentMemberResponse();
        ContentThumbResponse contentThumbResponse = new ContentThumbResponse();
        return GetUserLikeResponse.builder().id(contentLikes.getId()).
                createdAt(contentLikes.getCreatedDate().toString()).
                member(contentMemberResponse.contentUserToResponse(contentLikes.getUser())).
                content(contentThumbResponse.toDto(contentLikes.getContent())).build();
    }
}
