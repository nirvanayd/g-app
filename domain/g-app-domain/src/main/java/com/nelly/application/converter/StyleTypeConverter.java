package com.nelly.application.converter;

import com.nelly.application.enums.StyleType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class StyleTypeConverter implements AttributeConverter<StyleType, String> {

    @Override
    public String convertToDatabaseColumn(StyleType styleType) {
        if (styleType == null) return null;
        return styleType.getCode();
    }

    @Override
    public StyleType convertToEntityAttribute(String code) {
        if (code == null) return null;
        return Stream.of(StyleType.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
