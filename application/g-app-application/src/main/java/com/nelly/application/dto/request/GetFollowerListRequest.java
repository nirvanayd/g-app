package com.nelly.application.dto.request;

import lombok.Data;

@Data
public class GetFollowerListRequest extends PageRequest {
    String keyword;
}
