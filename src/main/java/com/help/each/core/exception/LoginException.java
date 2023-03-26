package com.help.each.core.exception;

import com.help.each.core.constant.Status;

/**
 * @author Yuanan
 * @date 2023/3/25
 * @description 登录异常类
 */
public class LoginException extends BaseException{
    public LoginException(Status status) {
        super(status);
    }

    public LoginException(Status status, Object data) {
        super(status, data);
    }

    public LoginException(Integer code, String message) {
        super(code, message);
    }

    public LoginException(Integer code, String message, Object data) {
        super(code, message, data);
    }
}
