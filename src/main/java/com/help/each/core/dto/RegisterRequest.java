package com.help.each.core.dto;

import com.help.each.core.validator.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * @author Yuanan
 * @date 2023/3/26
 * @description 用户信息注册验证DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotEmpty(message = "用户名不能为空")
    @Length(min = 3, max = 21, message = "用户名长度在3-21个字符之间")
    private String username;
    @NotEmpty(message = "密码不能为空")
    @Length(min = 8, max = 26, message = "密码的长度在8-26字符之间")
    private String password;
    @Role
    private String role;
    @Length(max = 21, message = "用户昵称最长为21个字符")
    private String nickname;
    @Pattern(regexp = "^男$|^女$",message = "用户的性别只能是男或女")
    private String sex;
    private String avatar;
    @Email(message = "邮箱格式不正确")
    private String email;
    @Pattern(regexp = "^((13[0-9])|(14(0|[5-7]|9))|(15([0-3]|[5-9]))|(16(2|[5-7]))|(17[0-8])|(18[0-9])|(19([0-3]|[5-9])))\\d{8}$", message = "手机号码格式不正确")
    private String phone;
    @Min(value = 0L, message = "年龄不能低于0岁")
    private Integer age;
}
