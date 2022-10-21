package com.nelly.application.dto.request;

import lombok.Data;

@Data
public class GetFollowingListRequest extends PageRequest {
    String keyword;
}
