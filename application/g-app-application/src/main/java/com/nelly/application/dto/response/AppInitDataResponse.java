package com.nelly.application.dto.response;

import com.nelly.application.domain.Agreements;
import dto.EnumStringCodeValue;
import lombok.Data;

import java.util.List;

@Data
public class AppInitDataResponse {
    private List<AgreementResponse> agreementsList;
    private List<EnumStringCodeValue> styleList;
}
