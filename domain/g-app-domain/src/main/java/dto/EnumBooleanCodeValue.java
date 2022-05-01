package dto;

import com.nelly.application.enums.enumInterface.CommonBooleanCode;
import lombok.Data;

@Data
public class EnumBooleanCodeValue {
    private boolean value;
    private String label;

    public EnumBooleanCodeValue(CommonBooleanCode commonBooleanCode) {
        this.value = commonBooleanCode.getCode();
        this.label = commonBooleanCode.getDesc();
    }
}
