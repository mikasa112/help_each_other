package com.help.each.core.constant;


import java.util.List;
import java.util.Locale;

/**
 * @author Yuanan
 * @date 2023/3/26
 * @description 用户权限的枚举
 */
public enum Role {
    USER,
    ADMIN;

    public static List<String> Roles() {
        return List.of(USER.name(), ADMIN.name(), USER.name().toLowerCase(Locale.ROOT), ADMIN.name().toLowerCase(Locale.ROOT));
    }

    /**
     * 判断权限是否相等
     *
     * @param role 权限名称
     * @return bool
     */
    public boolean equals(String role) {
        String name = this.name();
        return role.equals(name) || name.toLowerCase(Locale.ROOT).equals(role);
    }
}
