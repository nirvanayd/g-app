package com.nelly.application.dto.request;

import lombok.Data;

@Data
public class SearchTagContentRequest extends PageRequest {
    private Long id;
}
