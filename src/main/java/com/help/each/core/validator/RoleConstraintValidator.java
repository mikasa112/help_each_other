package com.help.each.core.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;

import java.util.Objects;

/**
 * @author Yuanan
 * @date 2023/3/26
 * @description 用户权限参数自定义注解
 */
@Slf4j
public class RoleConstraintValidator implements ConstraintValidator<Role, String> {

    @Override
    public boolean isValid(String role, ConstraintValidatorContext context) {
        if (Objects.isNull(role) || Strings.isEmpty(role)) {
            return false;
        }
        return com.help.each.core.constant.Role.Roles().contains(role);
    }
}
