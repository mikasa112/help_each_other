package com.help.each.service;

import com.help.each.core.dto.AuthenticationRequest;
import com.help.each.core.dto.RegisterRequest;
import com.help.each.core.vo.ApiResponse;
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
     * @return {@link ApiResponse}
     */
    ApiResponse authenticate(AuthenticationRequest request);

    /**
     * 注册
     *
     * @return {@link ApiResponse}
     */
    ApiResponse register(RegisterRequest request);

    /**
     * 登出
     *
     * @return {@link ApiResponse}
     */
    ApiResponse logout(HttpServletRequest request);
}
