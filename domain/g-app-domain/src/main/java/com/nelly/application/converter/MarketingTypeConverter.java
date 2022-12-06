package com.nelly.application.converter;

import com.nelly.application.enums.MarketingType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class MarketingTypeConverter implements AttributeConverter<MarketingType, String> {

    @Override
    public String convertToDatabaseColumn(MarketingType styleType) {
        if (styleType == null) return null;
        return styleType.getCode();
    }

    @Override
    public MarketingType convertToEntityAttribute(String code) {
        if (code == null) return null;
        return Stream.of(MarketingType.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
