package com.help.each.core.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author Yuanan
 * @date 2023/3/26
 * @description 自定义参数验证注解
 */
public class RememberConstraintValidator implements ConstraintValidator<Remember, Boolean> {
    @Override
    public void initialize(Remember constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Boolean aBoolean, ConstraintValidatorContext constraintValidatorContext) {
        return true;
    }
}
