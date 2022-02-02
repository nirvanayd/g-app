package dto;

import com.nelly.application.enums.enumInterface.CommonEnums;
import com.nelly.application.enums.enumInterface.CommonStringCode;
import lombok.Data;

@Data
public class EnumStringCodeValue {
    private String label;
    private String value;

    public EnumStringCodeValue(CommonStringCode commonStringCode) {
        this.label = commonStringCode.getCode();
        this.value = commonStringCode.getDesc();
    }

}
