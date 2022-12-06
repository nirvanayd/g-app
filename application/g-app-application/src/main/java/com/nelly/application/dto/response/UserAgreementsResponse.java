package com.nelly.application.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class UserAgreementsResponse {
    List<UserAgreementResponse> agreementList;
    List<String> marketingTypeList;
}