package com.nelly.application.dto.response;

import com.nelly.application.dto.CommonListResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetChildCommentListResponse extends CommonListResponse {
    private Long totalCount;
    private List<ChildCommentResponse> list;
}
