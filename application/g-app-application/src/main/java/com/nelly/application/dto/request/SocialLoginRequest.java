package com.nelly.application.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class SocialLoginRequest {
    @NotEmpty(message = "이메일은 필수값입니다.")
    private String email;
    @NotEmpty(message = "로그인 필수값이 누락되었습니다.[uid]")
    private String uid;
    @NotNull(message = "로그인 필수값이 누락되었습니다.[expire time]")
    private Long expireTime;
    @NotEmpty(message = "로그인 필수값이 누락되었습니다.[type]")
    private String type;
    private String accessToken;
    private String refreshToken;
    private String fcmToken;
}
