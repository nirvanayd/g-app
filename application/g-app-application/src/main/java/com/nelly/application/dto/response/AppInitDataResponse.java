package com.nelly.application.dto.response;

import com.nelly.application.domain.Agreements;
import dto.EnumStringCodeValue;
import lombok.Data;

import java.util.List;

@Data
public class AppInitDataResponse {
    private List<AgreementResponse> agreementsList;
    private List<EnumStringCodeValue> styleList;
    private List<BrandInitResponse> brandList;
    private List<EnumStringCodeValue> ageList;
    private List<EnumStringCodeValue> placeList;
    private List<ReportItemResponse> reportItemList;
}
