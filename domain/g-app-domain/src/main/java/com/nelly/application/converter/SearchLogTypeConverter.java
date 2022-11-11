package com.nelly.application.converter;

import com.nelly.application.enums.DisplayType;
import com.nelly.application.enums.SearchLogType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class SearchLogTypeConverter implements AttributeConverter<SearchLogType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(SearchLogType searchLogType) {
        if (searchLogType == null) return null;
        return searchLogType.getCode();
    }

    @Override
    public SearchLogType convertToEntityAttribute(Integer code) {
        if (code == null) return null;
        return Stream.of(SearchLogType.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}