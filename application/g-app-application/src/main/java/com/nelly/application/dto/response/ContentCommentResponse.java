package com.nelly.application.dto.response;

import com.nelly.application.dto.CommonListResponse;
import lombok.Data;

import java.util.List;

@Data
public class ContentCommentResponse {
    private Long count;
    private List<CommentResponse> list;
}
