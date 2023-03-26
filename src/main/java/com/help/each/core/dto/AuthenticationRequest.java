package com.help.each.core.dto;

import com.help.each.core.validator.Remember;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * @author Yuanan
 * @date 2023/3/26
 * @description 登录请求参数验证DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @NotEmpty(message = "用户名不能为空")
    @Length(min = 3, max = 21, message = "用户名长度在3-21个字符之间")
    private String username;
    @NotEmpty(message = "密码不能为空")
    @Length(min = 8, max = 26, message = "密码的长度在8-26字符之间")
    private String password;
    @Remember
    private boolean remember;
}
