package com.nelly.application.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nelly.application.enums.AgeType;
import lombok.Data;

@Data
public class BrandAgeResponse {
    private long id;
    @JsonIgnore
    private AgeType ageType;
    private String code;
    private String desc;

    public void setAgeType(AgeType ageType) {
        this.ageType = ageType;
        this.code = ageType.getCode();
        this.desc = ageType.getDesc();
    }
}
