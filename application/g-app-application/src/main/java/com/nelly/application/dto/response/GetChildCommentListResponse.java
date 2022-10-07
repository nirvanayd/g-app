package com.nelly.application.dto.response;

import com.nelly.application.dto.CommonListResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetChildCommentListResponse {
    private Long totalCount;
    private boolean hasNext;
    private List<ChildCommentResponse> list;
}
