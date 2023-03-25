package com.help.each.core.constant;

/**
 * @author Yuanan
 * @date 2023/3/23
 * @description 定义状态接口
 */
public interface IStatus {
    /**
     * 状态码
     *
     * @return 状态码
     */
    Integer getCode();

    /**
     * 返回信息
     *
     * @return 信息
     */
    String getMessage();
}
