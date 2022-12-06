package com.nelly.application.dto.request;

import com.nelly.application.enums.MarketingType;
import com.nelly.application.utils.annotation.EnumListValidator;
import com.nelly.application.utils.annotation.EnumValidator;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UpdateMarketingAgreementRequest {
    @NotNull
    @EnumValidator(enumClass = MarketingType.class, message = "마케팅 유형이 올바르지 않습니다.", enumMethod = "hasCode")
    private String userMarketingType;

    private String useYn;
}