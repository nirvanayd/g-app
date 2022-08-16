package com.nelly.application.dto;

import lombok.Data;

@Data
public class CommonResponse implements ResponseData {
    @Override
    public boolean isEnded() {
        return false;
    }
}
