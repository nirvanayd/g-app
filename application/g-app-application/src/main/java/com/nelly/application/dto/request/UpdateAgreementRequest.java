package com.nelly.application.dto.request;

import lombok.Data;

@Data
public class UpdateAgreementRequest {
    private String agreementType;
    private String useYn;
}