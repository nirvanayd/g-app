package com.nelly.application.dto.request;

import com.nelly.application.enums.StyleType;
import com.nelly.application.utils.annotation.EnumListValidator;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
public class UpdateUserRequest {

    @Pattern(regexp = "^[(http(s)?):\\/\\/(www\\.)?a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=()]*)$",
            message = "URL형식이 올바르지 않습니다.")
    private String profileImageUrl;

    private String profileTitle;
    private String profileText;

    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @Pattern(regexp = "^(19|20)\\d{2}$", message = "생년월일 양식이 맞지 않습니다.")
    private String birth;

    @EnumListValidator(enumClass = StyleType.class, message = "스타일 유형이 올바르지 않습니다.", enumMethod = "hasCode")
    private List<String> userStyle;
}
