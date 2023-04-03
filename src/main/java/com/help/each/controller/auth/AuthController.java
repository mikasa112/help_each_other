package com.help.each.controller.auth;

import com.help.each.core.constant.Status;
import com.help.each.core.dto.AuthenticationRequest;
import com.help.each.core.dto.RegisterRequest;
import com.help.each.core.vo.ApiResponse;
import com.help.each.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Yuanan
 * @date 2023/3/26
 * @description 验证控制器
 */
@Slf4j
@RestController
@RequestMapping("api/auth")
@Validated
@RequiredArgsConstructor
public class AuthController {
    final AuthenticationService service;

    @PostMapping("login")
    public ApiResponse login(@RequestBody @Valid AuthenticationRequest request) {
        return service.authenticate(request);
    }


    @PostMapping("register")
    public ApiResponse register(@RequestBody @Valid RegisterRequest request) {
        return service.register(request);
    }

    @PostMapping("logout")
    public ApiResponse logout(HttpServletRequest request) {
        return service.logout(request);
    }
}
