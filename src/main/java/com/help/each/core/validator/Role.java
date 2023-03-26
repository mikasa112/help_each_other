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
 * @description
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RoleConstraintValidator.class)
public @interface Role {
    String message() default "权限不存在";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    //权限
    String role() default "";
}
