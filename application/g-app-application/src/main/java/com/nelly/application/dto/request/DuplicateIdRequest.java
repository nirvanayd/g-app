package com.nelly.application.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class DuplicateIdRequest {
    @NotNull
    private String loginId;
}
