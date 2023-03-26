package com.help.each.core.exception;

import com.help.each.core.constant.Status;

/**
 * @author Yuanan
 * @date 2023/3/26
 * @description 注册异常
 */
public class RegisterException extends BaseException{
    public RegisterException(Status status) {
        super(status);
    }

    public RegisterException(Status status, Object data) {
        super(status, data);
    }

    public RegisterException(Integer code, String message) {
        super(code, message);
    }

    public RegisterException(Integer code, String message, Object data) {
        super(code, message, data);
    }
}
