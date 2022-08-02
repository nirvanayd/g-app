package com.nelly.application.dto.request;

import lombok.Data;

@Data
public class ReissueRequest {
    private String accessToken;
    private String refreshToken;
}
