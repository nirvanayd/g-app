package com.nelly.application.converter;

import com.nelly.application.enums.ScraperStatus;
import com.nelly.application.enums.UserStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class ScraperStatusConverter implements AttributeConverter<ScraperStatus, String> {

    @Override
    public String convertToDatabaseColumn(ScraperStatus scraperStatus) {
        if (scraperStatus == null) return null;
        return scraperStatus.getCode();
    }

    @Override
    public ScraperStatus convertToEntityAttribute(String code) {
        if (code == null) return null;

        return Stream.of(ScraperStatus.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
