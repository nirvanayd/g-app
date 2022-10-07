package com.nelly.application.dto.response;

import com.nelly.application.domain.Comments;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private List<ChildCommentResponse> commentList;
    private Integer status;
    private Integer childCount;

    public CommentResponse toDto(Comments c) {
        ContentMemberResponse memberResponse = new ContentMemberResponse();
        ChildCommentResponse childCommentResponse = new ChildCommentResponse();

        return CommentResponse.builder().
                id(c.getId()).
                comment(c.getComment()).
                createdAt(c.getCreatedDate().toString()).
                updatedAt(c.getModifiedDate().toString()).
                deletedAt(c.getDeletedDate() == null ? null : c.getDeletedDate().toString()).
                member(memberResponse.contentUserToResponse(c.getUser())).
                commentList(childCommentResponse.toDtoList(c.getComments())).build();
    }

    public List<CommentResponse> toDtoList(List<Comments> commentList) {
        ContentMemberResponse memberResponse = new ContentMemberResponse();
        ChildCommentResponse childCommentResponse = new ChildCommentResponse();
        return commentList.stream().map(c -> {

            int size = c.getComments().size();

            if (c.getComments().size() > 2) size = 2;
            List<Comments> childCommentList = c.getComments().subList(0,size);
            return CommentResponse.builder().
                    id(c.getId()).
                    comment(c.getComment()).
                    createdAt(c.getCreatedDate().toString()).
                    updatedAt(c.getModifiedDate().toString()).
                    deletedAt(c.getDeletedDate() == null ? null : c.getDeletedDate().toString()).
                    status(c.getStatus().getCode() == null ? null : Integer.parseInt(c.getStatus().getCode())).
                    member(memberResponse.contentUserToResponse(c.getUser())).
                    childCount(c.getComments().size()).
                    commentList(childCommentResponse.toDtoList(childCommentList)).build();
                }
        ).collect(Collectors.toList());
    }
}
