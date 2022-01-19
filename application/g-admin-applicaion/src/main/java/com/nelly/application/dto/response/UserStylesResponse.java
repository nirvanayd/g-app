package com.nelly.application.dto.response;

import com.nelly.application.enums.StyleType;
import lombok.Data;

@Data
public class UserStylesResponse {
    private long id;
    private StyleType styleType;
    private String styleCode;
    private String styleDesc;

    public void setStyleType(StyleType styleType) {
        this.styleType = styleType;
        this.styleCode = styleType.getCode();
        this.styleDesc = styleType.getDesc();
    }
}
