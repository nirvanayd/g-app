package com.nelly.application.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TokenInfoDto {
    private Long authId;
    private String loginId;
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long refreshTokenExpirationTime;

}
