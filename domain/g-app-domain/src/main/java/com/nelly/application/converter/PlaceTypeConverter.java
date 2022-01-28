package com.nelly.application.converter;

import com.nelly.application.enums.PlaceType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class PlaceTypeConverter implements AttributeConverter<PlaceType, String> {

    @Override
    public String convertToDatabaseColumn(PlaceType placeType) {
        if (placeType == null) return null;
        return placeType.getCode();
    }

    @Override
    public PlaceType convertToEntityAttribute(String code) {
        if (code == null) return null;
        return Stream.of(PlaceType.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
