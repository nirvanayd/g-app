package com.nelly.application.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nelly.application.enums.PlaceType;
import lombok.Data;

@Data
public class BrandPlaceResponse {
    private long id;
    @JsonIgnore
    private PlaceType placeType;
    private String code;
    private String desc;

    public void setPlaceType(PlaceType placeType) {
        this.placeType = placeType;
        this.code = placeType.getCode();
        this.desc = placeType.getDesc();
    }
}
