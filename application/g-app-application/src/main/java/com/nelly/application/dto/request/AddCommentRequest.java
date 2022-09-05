package com.nelly.application.dto.request;

import lombok.Data;

@Data
public class AddCommentRequest {

    private Long contentId;
    private String comment;
    private Long parentId;
}
