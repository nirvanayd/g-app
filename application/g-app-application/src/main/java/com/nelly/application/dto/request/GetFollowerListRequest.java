package com.nelly.application.dto.request;

import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class GetFollowerListRequest extends PageRequest {
    @Nullable
    String keyword;
}
