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
     * 服务访问次数
     */
    String SERVICE_VISITED_KEY_PREFIX = "service:visited_count:";

    Long DEFAULT_PAGE_SIZE = 10L;

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
    String SORT_BY = "id";

    /*
     * 令牌开头
     */
    String BEARER = "Bearer ";

    String SYS_POINT_REMARK_SUB = "系统扣除";
    String SYS_POINT_REMARK_ADD = "系统发放";
    String SYS_ORDER_FINISH = "订单完成";
}

