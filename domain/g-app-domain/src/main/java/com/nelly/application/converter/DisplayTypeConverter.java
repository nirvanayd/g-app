package com.nelly.application.converter;

import com.nelly.application.enums.AgeType;
import com.nelly.application.enums.DisplayType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class DisplayTypeConverter implements AttributeConverter<DisplayType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(DisplayType ageType) {
        if (ageType == null) return null;
        return ageType.getCode();
    }

    @Override
    public DisplayType convertToEntityAttribute(Integer code) {
        if (code == null) return null;
        return Stream.of(DisplayType.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
