package com.help.each.controller.filter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Sets;
import com.help.each.config.AppConfig;
import com.help.each.core.constant.Status;
import com.help.each.core.util.JWTUtil;
import com.help.each.core.vo.ApiResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

import com.help.each.core.exception.SecurityException;

/**
 * @author Yuanan
 * @date 2023/3/25
 * @description jwt验证过滤链
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    private final UserDetailsService userDetailsService;

    private final AppConfig appConfig;


    /**
     * 过滤器
     *
     * @param request     请求
     * @param response    响应
     * @param filterChain 链
     */
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (checkIgnores(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        final String token = jwtUtil.getJwtFromRequest(request);
        if (StrUtil.isNotBlank(token)) {
            try {
                String username = jwtUtil.extractUsername(token);
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                filterChain.doFilter(request, response);
            } catch (SecurityException e) {
                ApiResponse.PrintlnApiResponse(response, e);
            }
        } else {
            ApiResponse.PrintlnApiResponse(response, Status.UNAUTHORIZED);
        }
    }

    /**
     * 请求是否需要拦截
     *
     * @param request 请求
     */
    private boolean checkIgnores(HttpServletRequest request) {
        String method = request.getMethod();
        HttpMethod httpMethod = HttpMethod.valueOf(method);
        if (ObjectUtil.isNull(httpMethod)) {
            httpMethod = HttpMethod.GET;
        }
        Set<String> ignores = Sets.newHashSet();
        if (HttpMethod.GET.equals(httpMethod)) {
            ignores.addAll(appConfig.getIgnores().getGet());
        } else if (HttpMethod.PUT.equals(httpMethod)) {
            ignores.addAll(appConfig.getIgnores().getPut());
        } else if (HttpMethod.HEAD.equals(httpMethod)) {
            ignores.addAll(appConfig.getIgnores().getHead());
        } else if (HttpMethod.POST.equals(httpMethod)) {
            ignores.addAll(appConfig.getIgnores().getPost());
        } else if (HttpMethod.PATCH.equals(httpMethod)) {
            ignores.addAll(appConfig.getIgnores().getPatch());
        } else if (HttpMethod.TRACE.equals(httpMethod)) {
            ignores.addAll(appConfig.getIgnores().getTrace());
        } else if (HttpMethod.DELETE.equals(httpMethod)) {
            ignores.addAll(appConfig.getIgnores().getDelete());
        } else if (HttpMethod.OPTIONS.equals(httpMethod)) {
            ignores.addAll(appConfig.getIgnores().getOptions());
        }
        ignores.addAll(appConfig.getIgnores().getPattern());
        if (CollUtil.isNotEmpty(ignores)) {
            for (String ignore : ignores) {
                AntPathRequestMatcher matcher = new AntPathRequestMatcher(ignore, method);
                if (matcher.matches(request)) {
                    return true;
                }
            }
        }
        return false;
    }

}
