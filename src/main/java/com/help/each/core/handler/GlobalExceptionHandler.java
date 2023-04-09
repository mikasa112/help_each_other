package com.help.each.core.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.help.each.core.constant.Status;
import com.help.each.core.exception.BaseException;
import com.help.each.core.vo.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * @author Yuanan
 * @date 2023/3/23
 * @description 全局异常类处理类
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * @param e 异常
     * @return {@link ApiResponse}
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ApiResponse globalExceptionHandler(Exception e) {
        if (e instanceof NoHandlerFoundException) {
            log.error("【全局异常拦截】NoHandlerFoundException: 请求方法 {}, 请求路径 {}", ((NoHandlerFoundException) e).getRequestURL(), ((NoHandlerFoundException) e).getHttpMethod());
            return ApiResponse.OfStatus(Status.REQUEST_NOT_FOUND);
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            log.error("【全局异常拦截】HttpRequestMethodNotSupportedException: 当前请求方式 {}, 支持请求方式 {}", ((HttpRequestMethodNotSupportedException) e).getMethod(), JSONUtil.toJsonStr(((HttpRequestMethodNotSupportedException) e).getSupportedHttpMethods()));
            return ApiResponse.OfStatus(Status.HTTP_BAD_METHOD);
        } else if (e instanceof MethodArgumentNotValidException) {
            log.error("【全局异常拦截】MethodArgumentNotValidException", e);
            return ApiResponse.Of(Status.BAD_REQUEST.getCode(), ((MethodArgumentNotValidException) e).getBindingResult().getAllErrors().get(0).getDefaultMessage(), null);
        } else if (e instanceof ConstraintViolationException) {
            log.error("【全局异常拦截】ConstraintViolationException", e);
            return ApiResponse.Of(Status.BAD_REQUEST.getCode(), CollUtil.getFirst(((ConstraintViolationException) e).getConstraintViolations()).getMessage(), null);
        } else if (e instanceof MethodArgumentTypeMismatchException) {
            log.error("【全局异常拦截】MethodArgumentTypeMismatchException: 参数名 {}, 异常信息 {}", ((MethodArgumentTypeMismatchException) e).getName(), e.getMessage());
            return ApiResponse.OfStatus(Status.PARAM_NOT_MATCH);
        } else if (e instanceof HttpMessageNotReadableException) {
            log.error("【全局异常拦截】HttpMessageNotReadableException: 错误信息 {}", e.getMessage());
            return ApiResponse.OfStatus(Status.PARAM_NOT_NULL);
        } else if (e instanceof HttpMediaTypeNotSupportedException) {
            log.error("【全局异常拦截】HttpMediaTypeNotSupportedException: 错误信息 {}", e.getMessage());
            return ApiResponse.OfStatus(Status.HTTP_BAD_METHOD);
        } else if (e instanceof BadCredentialsException) {
            log.error("【全局异常拦截】BadCredentialsException: 错误信息 {}", e.getMessage());
            return ApiResponse.OfStatus(Status.USERNAME_PASSWORD_ERROR);
        } else if (e instanceof InternalAuthenticationServiceException) {
            log.error("【全局异常拦截】InternalAuthenticationServiceException:  异常信息 {}", e.getMessage());
            return ApiResponse.OfStatus(Status.USERNAME_PASSWORD_ERROR);
        } else if (e instanceof AccessDeniedException) {
            log.error("【全局异常拦截】AccessDeniedException:  异常信息 {}", e.getMessage());
            return ApiResponse.OfStatus(Status.ACCESS_DENIED);
        } else if (e instanceof BaseException) {
            log.error("【全局异常拦截】DataManagerException: 状态码 {}, 异常信息 {}", ((BaseException) e).getCode(), e.getMessage());
            return ApiResponse.OfException((BaseException) e);
        }
        log.error("【全局异常拦截】: 异常信息 {} ", e.getMessage());
        StackTraceElement ste = e.getStackTrace()[0];
        log.error("【全局异常拦截】: 异常类 {} ", ste.getClassName());
        log.error("【全局异常拦截】: 异常类名 {} ", ste.getFileName());
        log.error("【全局异常拦截】: 异常行号 {} ", ste.getLineNumber());
        log.error("【全局异常拦截】: 异常方法 {} ", ste.getMethodName());
        return ApiResponse.OfStatus(Status.ERROR);
    }

}
