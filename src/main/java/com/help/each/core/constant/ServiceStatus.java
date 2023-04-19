package com.help.each.core.constant;

import lombok.Getter;

/**
 * @author Yuanan
 * @date 2023/4/19
 * @description 服务当前的状态
 */
@Getter
public enum ServiceStatus {
    //未接单
    NOT_TAKE_SERVICE(0),
    //已接单
    TAKE_SERVICE(1),
    //已完成
    FINISH_SERVICE(2),
    ;

    private final Integer code;

    ServiceStatus(Integer code) {
        this.code = code;
    }
}
