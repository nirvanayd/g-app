package com.nelly.application.dto.request.common;

import lombok.Data;

@Data
public class ListRequest {
    private Integer page;
    private Integer size;

    public Integer getPage() {
        return page == null ? 0 : page;
    }

    public Integer getSize() {
        return size == null ? 10 : size;
    }
}
