package com.nelly.application.converter;

import com.nelly.application.enums.BrandStatus;
import com.nelly.application.enums.DeleteStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class DeleteStatusConverter implements AttributeConverter<DeleteStatus, String> {

    @Override
    public String convertToDatabaseColumn(DeleteStatus deleteStatus) {
        if (deleteStatus == null) return null;
        return deleteStatus.getCode();
    }

    @Override
    public DeleteStatus convertToEntityAttribute(String code) {
        if (code == null) return null;

        return Stream.of(DeleteStatus.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
