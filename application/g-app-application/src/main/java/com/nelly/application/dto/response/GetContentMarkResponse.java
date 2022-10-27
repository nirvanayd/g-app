package com.nelly.application.dto.response;

import com.nelly.application.domain.ContentLikes;
import com.nelly.application.domain.ContentMarks;
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
public class GetContentMarkResponse {
    private long id;
    private ContentMemberResponse member;
    private boolean follow;

    public GetContentMarkResponse toDto(ContentMarks contentMarks) {
        ContentMemberResponse contentMemberResponse = new ContentMemberResponse();

        return GetContentMarkResponse.builder().
                member(contentMemberResponse.contentUserToResponse(contentMarks.getUser())).
                id(contentMarks.getId()).build();
    }

    public List<GetContentMarkResponse> toDtoList(List<ContentMarks> contentMarkList) {
        return contentMarkList.stream().map(this::toDto).collect(Collectors.toList());
    }
}