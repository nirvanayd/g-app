package com.nelly.application.dto.request;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class FindIdRequest {
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
    private String email;
}
