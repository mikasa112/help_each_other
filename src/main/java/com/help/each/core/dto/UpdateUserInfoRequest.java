package com.help.each.core.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author Yuanan
 * @date 2023/4/3
 * @description 修改用户信息请求格式
 */
@Data
public class UpdateUserInfoRequest {
    @NotEmpty(message = "密码不能为空")
    @Length(min = 8, max = 26, message = "密码的长度在8-26字符之间")
    private String password;
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
    private int age;
}
