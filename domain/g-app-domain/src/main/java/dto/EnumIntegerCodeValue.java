package dto;

import com.nelly.application.enums.enumInterface.CommonIntegerCode;
import lombok.Data;

@Data
public class EnumIntegerCodeValue {
    private Integer value;
    private String label;

    public EnumIntegerCodeValue(CommonIntegerCode commonIntegerCode) {
        this.value = commonIntegerCode.getCode();
        this.label = commonIntegerCode.getDesc();
    }
}
