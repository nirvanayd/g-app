package com.nelly.application.dto.request;

import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class GetFollowingListRequest extends PageRequest {
    @Nullable
    String keyword;
}
