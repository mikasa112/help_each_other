package com.help.each.core.exception;

import com.help.each.core.constant.Status;

/**
 * @author Yuanan
 * @date 2023/3/25
 * @description 安全权限异常
 */
public class SecurityException extends BaseException{
    public SecurityException(Status status) {
        super(status);
    }

    public SecurityException(Status status, Object data) {
        super(status, data);
    }

    public SecurityException(Integer code, String message) {
        super(code, message);
    }

    public SecurityException(Integer code, String message, Object data) {
        super(code, message, data);
    }
}
