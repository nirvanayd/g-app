package com.nelly.application.converter;

import com.nelly.application.enums.IsUsedType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class IsUsedTypeConverter implements AttributeConverter<IsUsedType, Boolean> {

    @Override
    public Boolean convertToDatabaseColumn(IsUsedType isUsedType) {
        return isUsedType.getCode();
    }

    @Override
    public IsUsedType convertToEntityAttribute(Boolean code) {
        return Stream.of(IsUsedType.values())
                .filter(c -> c.getCode() == code)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
