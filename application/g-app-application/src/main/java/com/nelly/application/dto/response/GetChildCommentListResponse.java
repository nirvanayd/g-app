package com.nelly.application.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetChildCommentListResponse {
    private List<ChildCommentResponse> list;
}
