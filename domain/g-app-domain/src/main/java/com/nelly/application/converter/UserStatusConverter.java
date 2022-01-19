package com.nelly.application.converter;

import com.nelly.application.enums.UserStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class UserStatusConverter implements AttributeConverter<UserStatus, String> {

    @Override
    public String convertToDatabaseColumn(UserStatus userStatusCode) {
        if (userStatusCode == null) return null;
        return userStatusCode.getCode();
    }

    @Override
    public UserStatus convertToEntityAttribute(String code) {
        if (code == null) return null;

        return Stream.of(UserStatus.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
