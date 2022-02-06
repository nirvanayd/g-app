package com.nelly.application.converter;

import com.nelly.application.enums.BrandStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class BrandStatusConverter implements AttributeConverter<BrandStatus, String> {

    @Override
    public String convertToDatabaseColumn(BrandStatus brandStatus) {
        if (brandStatus == null) return null;
        return brandStatus.getCode();
    }

    @Override
    public BrandStatus convertToEntityAttribute(String code) {
        if (code == null) return null;

        return Stream.of(BrandStatus.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
