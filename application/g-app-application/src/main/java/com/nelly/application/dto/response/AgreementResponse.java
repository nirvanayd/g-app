package com.nelly.application.dto.response;

import lombok.Data;

@Data
public class AgreementResponse {
    private String agreementTypeCode;
    private String version;
    private String title;
    private String isRequired;
    private String content;
}
