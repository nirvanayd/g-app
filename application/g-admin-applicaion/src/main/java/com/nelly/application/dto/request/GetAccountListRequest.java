package com.nelly.application.dto.request;

import com.nelly.application.dto.request.common.ListRequest;
import com.nelly.application.enums.BrandStatus;
import com.nelly.application.enums.DisplayType;
import com.nelly.application.enums.UserStatus;
import com.nelly.application.utils.annotation.EnumIntegerValidator;
import com.nelly.application.utils.annotation.EnumValidator;
import lombok.Data;

@Data
public class GetAccountListRequest extends ListRequest {
    private String loginId;
    private String email;
    @EnumIntegerValidator(enumClass = UserStatus.class, message = "상태값이 올바르지 않습니다.", enumMethod = "hasCodeValueExist")
    private String statusCode;
}
