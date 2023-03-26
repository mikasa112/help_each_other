package com.help.each.service;

import com.help.each.core.dto.AuthenticationRequest;
import com.help.each.core.dto.RegisterRequest;
import com.help.each.entity.User;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author Yuanan
 * @date 2023/3/25
 * @description 身份验证服务
 */
public interface AuthenticationService {
    /**
     * 用户登录
     *
     * @return token
     */
    String authenticate(AuthenticationRequest request);

    /**
     * 注册
     *
     * @return 是否成功
     */
    boolean register(RegisterRequest request);

    /**
     * 登出
     *
     * @return 是否成功
     */
    boolean logout(HttpServletRequest request);
}
