package com.nelly.application.converter;

import com.nelly.application.enums.AgreementType;
import com.nelly.application.enums.UserStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class AgreementTypeConverter implements AttributeConverter<AgreementType, String> {

    @Override
    public String convertToDatabaseColumn(AgreementType agreementType) {
        if (agreementType == null) return null;
        return agreementType.getCode();
    }

    @Override
    public AgreementType convertToEntityAttribute(String code) {
        if (code == null) return null;

        return Stream.of(AgreementType.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
