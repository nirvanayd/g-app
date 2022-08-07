package com.nelly.application.dto.response;

import dto.EnumStringCodeValue;
import lombok.Data;

import java.util.List;

@Data
public class AgreementResponse {
    private String agreementTypeCode;
    private String version;
    private String title;
    private String isRequired;
    private String content;
    private List<EnumStringCodeValue> itemList;
}
