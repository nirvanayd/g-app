package com.nelly.application.dto.request;

import com.nelly.application.enums.MarketingType;
import com.nelly.application.enums.StyleType;
import com.nelly.application.utils.annotation.EnumListValidator;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
public class SignUpRequest {
    @NotEmpty(message = "아이디는 필수 입력값입니다.")
    private String loginId;

    @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @NotEmpty(message = "생년월일은 필수 입력값입니다.")
    @Pattern(regexp = "^(19[0-9][0-9]|20\\d{2})(0[0-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])$", message = "생년월일 양식이 맞지 않습니다.")
    private String birth;

    @EnumListValidator(enumClass = StyleType.class, message = "스타일 유형이 올바르지 않습니다.", enumMethod = "hasCode")
    private List<String> userStyle;

    @EnumListValidator(enumClass = MarketingType.class, message = "마케팅 유형이 올바르지 않습니다.", enumMethod = "hasCode")
    private List<String> userMarketingType;

    private List<UserAgreementRequest> agreementList;
}
