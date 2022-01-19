package com.nelly.application.dto;

import lombok.Data;

@Data
public class AuthTokenInfoDto {
    private Long authId;
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long refreshTokenExpirationTime;
}
