package com.nelly.application.dto.request;

import lombok.Data;

@Data
public class AddCommentRequest {
    private String comment;
    private Long tagUserId;
    private Long parentId;
}
