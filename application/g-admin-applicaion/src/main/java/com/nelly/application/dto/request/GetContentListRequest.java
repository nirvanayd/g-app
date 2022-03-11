package com.nelly.application.dto.request;

import com.nelly.application.dto.request.common.ListRequest;
import com.nelly.application.enums.YesOrNoType;
import com.nelly.application.utils.annotation.EnumValidator;
import lombok.Data;

@Data
public class GetContentListRequest extends ListRequest {

    private String loginId;
    @EnumValidator(enumClass = YesOrNoType.class, message = "상태값이 올바르지 않습니다.", enumMethod = "hasCodeValueExist")
    private String isDeleted;
}
