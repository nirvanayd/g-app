package com.nelly.application.dto.request;

import com.nelly.application.enums.AgeType;
import com.nelly.application.enums.BrandStatus;
import com.nelly.application.enums.PlaceType;
import com.nelly.application.enums.StyleType;
import com.nelly.application.utils.annotation.EnumListValidator;
import lombok.Data;
import net.bytebuddy.implementation.bind.annotation.Empty;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
public class AddBrandRequest {
    private Long id;
    @NotEmpty(message = "이름은 필수값입니다.")
    private String name;
    private String description;
    private BrandStatus status;
    private Integer isDisplay;
    @NotEmpty(message = "홈페이지URL은 필수값입니다.")
    @Pattern(regexp = "^[(http(s)?):\\/\\/(www\\.)?a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=()]*)$",
            message = "URL형식이 올바르지 않습니다.")
    private String homepage;
    @Pattern(regexp = "^[(http(s)?):\\/\\/(www\\.)?a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=()]*)$",
            message = "URL형식이 올바르지 않습니다.")
    private String logoImageUrl;
    @Pattern(regexp = "^[(http(s)?):\\/\\/(www\\.)?a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=()]*)$",
            message = "URL형식이 올바르지 않습니다.")
    private String introduceImageUrl;
    @EnumListValidator(enumClass = StyleType.class, message = "스타일 유형이 올바르지 않습니다.", enumMethod = "hasCode")
    private List<String> brandStyle;

    @EnumListValidator(enumClass = AgeType.class, message = "나이 유형이 올바르지 않습니다.", enumMethod = "hasCode")
    private List<String> ageType;

    @EnumListValidator(enumClass = PlaceType.class, message = "장소 유형이 올바르지 않습니다.", enumMethod = "hasCode")
    private List<String> placeType;
}
