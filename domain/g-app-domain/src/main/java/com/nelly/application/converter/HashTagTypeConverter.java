package com.nelly.application.converter;

import com.nelly.application.enums.HashTagType;
import com.nelly.application.enums.UserStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class HashTagTypeConverter implements AttributeConverter<HashTagType, String> {

    @Override
    public String convertToDatabaseColumn(HashTagType hashTagType) {
        if (hashTagType == null) return null;
        return hashTagType.getCode();
    }

    @Override
    public HashTagType convertToEntityAttribute(String code) {
        if (code == null) return null;

        return Stream.of(HashTagType.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
