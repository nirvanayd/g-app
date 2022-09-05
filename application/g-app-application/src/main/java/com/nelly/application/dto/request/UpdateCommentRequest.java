package com.nelly.application.dto.request;

import lombok.Data;

@Data
public class UpdateCommentRequest {
    private Long id;
    private String comment;
}
