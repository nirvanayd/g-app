package com.nelly.application.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nelly.application.enums.StyleType;
import lombok.Data;

@Data
public class BrandStyleResponse {
    private long id;
    @JsonIgnore
    private StyleType styleType;
    private String code;
    private String desc;

    public void setStyleType(StyleType styleType) {
        this.styleType = styleType;
        this.code = styleType.getCode();
        this.desc = styleType.getDesc();
    }
}
