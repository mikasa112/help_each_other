package com.help.each.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.help.each.core.constant.Status;
import com.help.each.core.dto.AuthenticationRequest;
import com.help.each.core.dto.RegisterRequest;
import com.help.each.core.exception.RegisterException;
import com.help.each.core.util.JWTUtil;
import com.help.each.core.vo.ApiResponse;
import com.help.each.entity.MyUserDetails;
import com.help.each.entity.User;
import com.help.each.mapper.UserMapper;
import com.help.each.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author Yuanan
 * @date 2023/3/26
 * @description 授权服务实现类
 */
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    final UserMapper userMapper;
    final PasswordEncoder passwordEncoder;
    final JWTUtil jwtUtil;
    final AuthenticationManager authenticationManager;


    @Override
    public ApiResponse authenticate(AuthenticationRequest request) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        UserDetails userDetails = (UserDetails) authenticate.getPrincipal();
        MyUserDetails myUserDetails = (MyUserDetails) userDetails;
        return ApiResponse.OfSuccess(jwtUtil.generateToken(myUserDetails.getUser(), userDetails, request.isRemember()));
    }

    @Override
    @CacheEvict(value = "user:page", allEntries = true)
    public ApiResponse register(RegisterRequest request) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(User::getUsername, request.getUsername());
        User one = userMapper.selectOne(wrapper);
        if (Objects.nonNull(one)) {
            throw new RegisterException(Status.USERNAME_ALREADY_EXISTS);
        }
        User user = new User();
        user.setUuid(IdUtil.randomUUID());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setNickname(request.getNickname());
        user.setAvatar(request.getAvatar());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setAge(request.getAge());
        user.setSex(request.getSex());
        user.setPoints(0F);
        return ApiResponse.PrintlnApiResponse(userMapper.insert(user) >= 1, "注册成功", Status.ERROR);
    }

    @Override
    public ApiResponse logout(HttpServletRequest request) {
        return ApiResponse.PrintlnApiResponse(jwtUtil.invalidateJWT(request), "退出成功", Status.ERROR);
    }


}
