package com.nelly.application.dto.request;

import lombok.Data;

@Data
public class PageRequest {
    private Integer page;
    private Integer size;

    public Integer getPage() {
        if (page ==  null) return 0;
        return page;
    }

    public Integer getSize() {
        if (size == null) return 20;
        return size;
    }
}
