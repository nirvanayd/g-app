package com.nelly.application.dto.response;

import com.nelly.application.domain.Comments;
import com.nelly.application.domain.Contents;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private long id;
    private ContentMemberResponse member;
    private String comment;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;
    private List<CommentResponse> commentList;

    public List<CommentResponse> toDtoList(List<Comments> commentList) {
        ContentMemberResponse memberResponse = new ContentMemberResponse();

        return commentList.stream().map(c -> CommentResponse.builder().
                id(c.getId()).
                comment(c.getComment()).
                createdAt(c.getCreatedDate().toString()).
                updatedAt(c.getModifiedDate().toString()).
                deletedAt(c.getDeletedDate() == null ? null : c.getDeletedDate().toString()).
                member(memberResponse.contentUserToResponse(c.getUser())).
                commentList(toDtoList(c.getComments(), true)).build()
        ).collect(Collectors.toList());
    }

    public List<CommentResponse> toDtoList(List<Comments> commentList, boolean child) {
        ContentMemberResponse memberResponse = new ContentMemberResponse();

        return commentList.stream().sorted(Comparator.comparing(Comments::getId)).map(c -> CommentResponse.builder().
                id(c.getId()).
                comment(c.getComment()).
                createdAt(c.getCreatedDate().toString()).
                updatedAt(c.getModifiedDate().toString()).
                deletedAt(c.getDeletedDate() == null ? null : c.getDeletedDate().toString()).
                member(memberResponse.contentUserToResponse(c.getUser())).build()
        ).collect(Collectors.toList());
    }
}
