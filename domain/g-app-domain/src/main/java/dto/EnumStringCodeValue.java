package dto;

import com.nelly.application.enums.enumInterface.CommonEnums;
import com.nelly.application.enums.enumInterface.CommonStringCode;
import lombok.Data;

@Data
public class EnumStringCodeValue {
    private String code;
    private String desc;

    public EnumStringCodeValue(CommonStringCode commonStringCode) {
        this.code = commonStringCode.getCode();
        this.desc = commonStringCode.getDesc();
    }

}
