package com.nelly.application.utils.annotation;

import com.nelly.application.enums.enumInterface.CommonEnums;
import com.nelly.application.utils.annotation.aspect.EnumIntegerValidatorAspect;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {EnumIntegerValidatorAspect.class})
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumIntegerValidator {
    String message() default "잘못된 접근 입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    Class<? extends CommonEnums> enumClass();
    String enumMethod();
    boolean ignoreCase() default false;


}
