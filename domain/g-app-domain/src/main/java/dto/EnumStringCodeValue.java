package dto;

import com.nelly.application.enums.enumInterface.CommonEnums;
import com.nelly.application.enums.enumInterface.CommonStringCode;
import lombok.Data;

@Data
public class EnumStringCodeValue {
    private String value;
    private String label;

    public EnumStringCodeValue(CommonStringCode commonStringCode) {
        this.value = commonStringCode.getCode();
        this.label = commonStringCode.getDesc();
    }

}
