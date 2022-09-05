package com.nelly.application.dto;

import lombok.Data;

@Data
public class CommonListResponse implements ResponseData {
    private boolean isEnded;
    public boolean isEnded() {
        return isEnded;
    }
}
