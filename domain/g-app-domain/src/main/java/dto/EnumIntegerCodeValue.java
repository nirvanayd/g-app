package dto;

import com.nelly.application.enums.enumInterface.CommonIntegerCode;
import lombok.Data;

@Data
public class EnumIntegerCodeValue {
    private Integer code;
    private String desc;

    public EnumIntegerCodeValue(CommonIntegerCode commonIntegerCode) {
        this.code = commonIntegerCode.getCode();
        this.desc = commonIntegerCode.getDesc();
    }
}
