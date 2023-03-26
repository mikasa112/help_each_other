package com.help.each.core.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Yuanan
 * @date 2023/3/26
 * @description 是否记住我注解
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RememberConstraintValidator.class)
public @interface Remember {
    String message() default "记住我参数错误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean type() default false;
}