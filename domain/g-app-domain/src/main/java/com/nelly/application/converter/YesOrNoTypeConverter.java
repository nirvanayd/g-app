package com.nelly.application.converter;

import com.nelly.application.enums.UserStatus;
import com.nelly.application.enums.YesOrNoType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class YesOrNoTypeConverter implements AttributeConverter<YesOrNoType, String> {

    @Override
    public String convertToDatabaseColumn(YesOrNoType yesOrNoType) {
        if (yesOrNoType == null) return null;
        return yesOrNoType.getCode();
    }

    @Override
    public YesOrNoType convertToEntityAttribute(String code) {
        if (code == null) return null;

        return Stream.of(YesOrNoType.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
