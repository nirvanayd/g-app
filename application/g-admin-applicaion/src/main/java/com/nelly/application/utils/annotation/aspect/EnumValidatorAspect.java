package com.nelly.application.utils.annotation.aspect;

import com.nelly.application.utils.annotation.EnumValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class EnumValidatorAspect implements ConstraintValidator<EnumValidator, String> {
    private Class<? extends Enum<?>> enumClass;
    private String enumMethod;

    @Override
    public void initialize(EnumValidator constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
        this.enumMethod = constraintAnnotation.enumMethod();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        try {
            Method method = this.hasValidateMethod();
            if (method != null) {
                Boolean result = (Boolean) method.invoke(null, value);
                return result != null && result;
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