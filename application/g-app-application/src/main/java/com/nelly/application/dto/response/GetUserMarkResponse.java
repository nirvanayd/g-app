package com.nelly.application.dto.response;

import com.nelly.application.domain.ContentLikes;
import com.nelly.application.domain.ContentMarks;
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
public class GetUserMarkResponse {
    private long id;
    private ContentMemberResponse member;
    private ContentThumbResponse content;

    public List<GetUserMarkResponse> toDtoList(List<ContentMarks> contentMarkList) {
        return contentMarkList.stream().map(this::toDto).collect(Collectors.toList());
    }

    public GetUserMarkResponse toDto(ContentMarks contentMark) {
        ContentMemberResponse contentMemberResponse = new ContentMemberResponse();
        ContentThumbResponse contentThumbResponse = new ContentThumbResponse();
        return GetUserMarkResponse.builder().id(contentMark.getId()).
                member(contentMemberResponse.contentUserToResponse(contentMark.getUser())).
                content(contentThumbResponse.toDto(contentMark.getContent())).build();
    }
}
