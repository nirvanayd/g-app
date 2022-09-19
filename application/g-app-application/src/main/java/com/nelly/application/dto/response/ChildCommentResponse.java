package com.nelly.application.dto.response;

import com.nelly.application.domain.Comments;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.implementation.bind.annotation.Empty;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChildCommentResponse {
    private long id;
    private ContentMemberResponse member;
    private String comment;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;
    private Integer status;

    public List<ChildCommentResponse> toDtoList(List<Comments> commentList) {
        if (commentList == null) return Collections.<ChildCommentResponse>emptyList();
        ContentMemberResponse memberResponse = new ContentMemberResponse();

        return commentList.stream().map(c -> ChildCommentResponse.builder().
                id(c.getId()).
                comment(c.getComment()).
                createdAt(c.getCreatedDate().toString()).
                updatedAt(c.getModifiedDate().toString()).
                deletedAt(c.getDeletedDate() == null ? null : c.getDeletedDate().toString()).
                member(memberResponse.contentUserToResponse(c.getUser())).
                status(c.getStatus().getCode() == null ? null : Integer.parseInt(c.getStatus().getCode())).
                build()
        ).collect(Collectors.toList());
    }
}
