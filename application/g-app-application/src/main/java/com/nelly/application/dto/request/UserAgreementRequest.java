package com.nelly.application.dto.request;

import lombok.Data;

@Data
public class UserAgreementRequest {
    private String agreementType;
    private String value;
}
