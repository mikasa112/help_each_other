package com.help.each.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.help.each.core.constant.Status;
import com.help.each.core.util.RedisUtil;
import com.help.each.core.vo.ApiResponse;
import com.help.each.entity.Order;
import com.help.each.mapper.OrderMapper;
import com.help.each.mapper.ServiceMapper;
import com.help.each.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Yuanan
 * @date 2023/4/14
 * @description 订单服务实现
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    final OrderMapper orderMapper;
    final ServiceMapper serviceMapper;
    final RedisUtil redisUtil;
    final RedisTemplate<Object, Object> redisTemplate;


    @Override
    public ApiResponse takeOrder(String uuid, Long serviceId) {
        com.help.each.entity.Service customer = serviceMapper.selectOne(Wrappers.lambdaQuery(com.help.each.entity.Service.class)
                .eq(com.help.each.entity.Service::getServiceId, serviceId)
                .select(com.help.each.entity.Service::getUuid));
        Order order = new Order();
        order.setOrderId(IdUtil.getSnowflakeNextId())
                .setServiceId(serviceId)
                .setProviderUuid(uuid)
                .setCustomerUuid(customer.getUuid())
                .setStatus(0);
        redisTemplate.convertAndSend("order", order);
//        if (orderMapper.insert(order) >= 1) {
//            redisUtil.setObject(ORDER_KEY_PREFIX + order.getOrderId(), order.toJSONString());
//            return ApiResponse.OfStatus(Status.OK);
//        }
        return ApiResponse.OfStatus(Status.OK);
    }

    @Override
    public ApiResponse confimOrder(String uuid, Long serviceId) {
        return null;
    }

    @Override
    public ApiResponse finishOrder(String uuid, Long serviceId) {
        return null;
    }

    @Override
    public ApiResponse getAllOrders(Long currentPage, Long pageSize, String sortBy, String order) {
        return null;
    }

    @Override
    public ApiResponse getOrdersByUUID(String uuid, Long currentPage, Long pageSize, String sortBy, String order) {
        return null;
    }

    @Override
    public ApiResponse removeOrder(String uuid, Integer orderId) {
        return null;
    }
}
