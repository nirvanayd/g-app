package com.nelly.application.converter;

import com.nelly.application.enums.AgeType;
import com.nelly.application.enums.StyleType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class AgeTypeConverter implements AttributeConverter<AgeType, String> {

    @Override
    public String convertToDatabaseColumn(AgeType ageType) {
        if (ageType == null) return null;
        return ageType.getCode();
    }

    @Override
    public AgeType convertToEntityAttribute(String code) {
        if (code == null) return null;
        return Stream.of(AgeType.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
