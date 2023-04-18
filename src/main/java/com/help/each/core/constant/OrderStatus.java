package com.help.each.core.constant;

import lombok.Getter;

/**
 * @author Yuanan
 * @date 2023/4/18
 * @description
 */
@Getter
public enum OrderStatus {
    //等待服务消费者同意
    AWAIT(0),
    //工作中
    WORKING(1),
    //完成
    FINISH(2),
    //异常
    EXCEPTION(3),
    //取消
    CANCEL(4),
    ;

    private final Integer code;

    OrderStatus(Integer code) {
        this.code = code;
    }
}
