package com.help.each.core.constant;

import lombok.Getter;

/**
 * @author Yuanan
 * @date 2023/3/23
 * @description 通用状态码
 */
@Getter
public enum Status implements IStatus {
    /*
     * 系统基本信息
     */
    OK(200, "操作成功"),
    ERROR(500, "操作异常"),
    PARAM_NOT_MATCH(400, "参数不匹配"),
    PARAM_NOT_NULL(400, "参数不能为空"),
    UNAUTHORIZED(401, "请先登录"),
    ACCESS_DENIED(403, "权限不足"),
    REQUEST_NOT_FOUND(404, "请求资源不存在"),
    HTTP_BAD_METHOD(405, "请求方式不支持"),
    BAD_REQUEST(400, "请求异常"),
    /*
     * 系统常驻服务
     */
    TOKEN_EXPIRED(5001, "token已过期,请重新登录"),
    TOKEN_PARSE_ERROR(5002, "token解析失败,请重新登录"),
    USERNAME_PASSWORD_ERROR(5003, "用户名或密码错误"),
    TOKEN_OUT_OF_CTRL(5003, "当前用户已在别处登录，请尝试更改密码或重新登录！"),
    USERNAME_ALREADY_EXISTS(5004, "当前用户名已存在，请重新输入"),
    FILE_CONTENT_IS_EMPTY(5005, "文件内容为空"),
    FILE_UPLOAD_FAILED(5006, "文件上传失败"),
    USER_UPDATE_FAILED(5007, "用户信息更新失败"),
    USER_REMOVE_FAILED(5008, "用户删除失败"),
    /*
     * 服务的状态
     */
    SERVICE_CREATE_FAILED(6001, "服务创建失败"),
    SERVICE_REMOVE_FAILED(6002, "服务删除失败"),
    SERVICE_UPDATE_FAILED(6003, "服务更新失败"),

    ;

    private final Integer code;
    private final String message;

    Status(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return String.format(" Status:{code=%s, message=%s} ", getCode(), getMessage());
    }
}
