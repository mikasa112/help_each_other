package com.help.each.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.help.each.core.vo.ApiResponse;
import com.help.each.entity.Order;

/**
 * @author Yuanan
 * @date 2023/4/14
 * @description 订单服务
 */
public interface OrderService extends IService<Order> {

    /**
     * 用户接单
     *
     * @param uuid      用户UUID
     * @param serviceId 服务ID
     */
    ApiResponse takeOrder(String uuid, Long serviceId);

    /**
     * 用户确认服务者
     *
     * @param uuid      用户UUID
     * @param serviceId 服务ID
     */
    ApiResponse confimOrder(String uuid, Long serviceId);

    /**
     * 用户完成服务工作订单
     *
     * @param uuid      用户UUID
     * @param serviceId 服务ID
     */
    ApiResponse finishOrder(String uuid, Long serviceId);

    /**
     * 查询全部的订单(管理员操作)
     *
     * @param currentPage 当前页
     * @param pageSize    总页数
     * @param sortBy      以什么字段排序
     * @param order       默认升序
     */
    ApiResponse getAllOrders(Long currentPage, Long pageSize, String sortBy, String order);

    /**
     * 查询自己的订单
     *
     * @param uuid        用户UUID
     * @param currentPage 当前页
     * @param pageSize    总页数
     * @param sortBy      以什么字段排序
     * @param order       默认升序
     */
    ApiResponse getOrdersByUUID(String uuid, Long currentPage, Long pageSize, String sortBy, String order);


    /**
     * 删除订单
     *
     * @param uuid    用户UUID
     * @param orderId 订单ID
     */
    ApiResponse removeOrder(String uuid, Integer orderId);
}
