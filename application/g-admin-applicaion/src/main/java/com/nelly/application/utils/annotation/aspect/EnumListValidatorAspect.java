package com.nelly.application.utils.annotation.aspect;

import com.nelly.application.utils.annotation.EnumListValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

public class EnumListValidatorAspect implements ConstraintValidator<EnumListValidator, List<String>> {
    private Class<? extends Enum<?>> enumClass;
    private String enumMethod;

    @Override
    public void initialize(EnumListValidator constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
        this.enumMethod = constraintAnnotation.enumMethod();
    }

    @Override
    public boolean isValid(List<String> value, ConstraintValidatorContext context) {
        // 값이 없는 경우 통과
        if (value == null || value.isEmpty()) return true;
        try {
            Method method = this.hasValidateMethod();
            if (method != null) {
                for (String v : value) {
                    Boolean result = (Boolean) method.invoke(null, v);
                    if (!result) return false;
                }
                return true;
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            return false;
        }
        return false;
    }

    private Method hasValidateMethod() {
        try {
            Method method = this.enumClass.getMethod(this.enumMethod, String.class);
            if (!Boolean.TYPE.equals(method.getReturnType()) && !Boolean.class.equals(method.getReturnType())) {
                throw new RuntimeException();
            }
            if(!Modifier.isStatic(method.getModifiers())) {
                throw new RuntimeException();
            }
            return method;
        } catch (NoSuchMethodException | RuntimeException ne) {
            return null;
        }
    }
}