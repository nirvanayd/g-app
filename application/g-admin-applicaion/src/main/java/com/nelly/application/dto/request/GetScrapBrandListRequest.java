package com.nelly.application.dto.request;

import com.nelly.application.dto.request.common.ListRequest;
import com.nelly.application.enums.BrandStatus;
import com.nelly.application.enums.DisplayType;
import com.nelly.application.enums.IsUsedType;
import com.nelly.application.enums.ScraperStatus;
import com.nelly.application.utils.annotation.EnumIntegerValidator;
import com.nelly.application.utils.annotation.EnumValidator;
import lombok.Data;

@Data
public class GetScrapBrandListRequest extends ListRequest {
    private String name;
    @EnumValidator(enumClass = ScraperStatus.class, message = "상태값이 올바르지 않습니다.", enumMethod = "hasCodeValueExist")
    private String statusCode;

    @EnumValidator(enumClass = IsUsedType.class, message = "상태값이 올바르지 않습니다.", enumMethod = "hasCodeValueExist")
    private Integer isUsed;
}
