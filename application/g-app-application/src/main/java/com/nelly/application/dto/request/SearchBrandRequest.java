package com.nelly.application.dto.request;

import com.nelly.application.domain.BrandStyles;
import com.nelly.application.enums.AgeType;
import com.nelly.application.enums.PlaceType;
import com.nelly.application.enums.StyleType;
import com.nelly.application.utils.annotation.EnumListValidator;
import lombok.Data;

import java.util.List;

@Data
public class SearchBrandRequest extends PageRequest {
    private String brandName;

    @EnumListValidator(enumClass = StyleType.class, message = "스타일 유형이 올바르지 않습니다.", enumMethod = "hasCode")
    private List<String> style;

    @EnumListValidator(enumClass = PlaceType.class, message = "장소 유형이 올바르지 않습니다.", enumMethod = "hasCode")
    private List<String> place;

    @EnumListValidator(enumClass = AgeType.class, message = "장소 유형이 올바르지 않습니다.", enumMethod = "hasCode")
    private List<String> age;
}
