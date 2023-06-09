package com.help.each.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.help.each.core.constant.Status;
import com.help.each.core.exception.LoginException;
import com.help.each.core.util.LocalStaticCache;
import com.help.each.entity.MyUserDetails;
import com.help.each.entity.User;
import com.help.each.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;


/**
 * @author Yuanan
 * @date 2023/3/25
 * @description security UsersDetailService
 */
@RequiredArgsConstructor
@Configuration
@Slf4j
public class SecurityConfig {
    private final UserMapper userMapper;

    /**
     * @return {@link UserDetailsService}
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            //将user缓存避免每次请求都会查询
            User user = (User) LocalStaticCache.getInstance().getObject(username);
            if (Objects.isNull(user)) {
                User u = userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getUsername, username));
                if (Objects.isNull(u)) {
                    throw new LoginException(Status.USERNAME_PASSWORD_ERROR);
                }
                LocalStaticCache.getInstance().setObject(username, u);
                user = u;
            }
            return new MyUserDetails(user);
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
