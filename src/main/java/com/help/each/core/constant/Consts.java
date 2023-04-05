package com.help.each.core.constant;


/**
 * @author Yuanan
 * @date 2023/3/23
 * @description 常量
 */
public interface Consts {
    /*
     * 存入redis的token的前缀
     */
    String REDIS_JWT_KEY_PREFIX = "security:jwt:";

    /*
     * 升序
     */
    String ASC_ORDER = "asc";

    /*
     * 降序
     */
    String DESC_ORDER = "desc";

    /*
     * 默认以id排序
     */
    String SORT_BY="id";

    /*
     * 令牌开头
     */
    String BEARER = "Bearer ";
}
