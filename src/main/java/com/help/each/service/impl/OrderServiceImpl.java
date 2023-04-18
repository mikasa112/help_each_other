package com.help.each.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.help.each.core.constant.Status;
import com.help.each.core.util.RedisUtil;
import com.help.each.core.vo.ApiResponse;
import com.help.each.core.vo.PageResult;
import com.help.each.entity.Order;
import com.help.each.mapper.OrderMapper;
import com.help.each.mapper.ServiceMapper;
import com.help.each.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    private static final String ORDER_KEY = "orders:";

    @Override
    @CacheEvict(value = "orders:page", allEntries = true)
    public ApiResponse takeOrder(String uuid, Long serviceId) {
        com.help.each.entity.Service customer = serviceMapper.selectOne(Wrappers.lambdaQuery(com.help.each.entity.Service.class)
                .eq(com.help.each.entity.Service::getServiceId, serviceId)
                .select(com.help.each.entity.Service::getUuid));
        Order order = new Order();
        order.setOrderId(IdUtil.getSnowflakeNextId())
                .setServiceId(serviceId)
                .setProviderUuid(uuid)
                .setCustomerUuid(customer.getUuid())
                .setStatus(0)
                .setPay(0);
        //推送订单已接单
        redisTemplate.convertAndSend("order", order);
        //用户30分钟不接单就取消
        redisUtil.setObject(ORDER_KEY + order.getOrderId(), order, 30, TimeUnit.MINUTES);
        return ApiResponse.OfStatus(Status.OK);
    }

    @Override
    public ApiResponse confimOrder(String uuid, Long orderId) {
        Object o = redisUtil.getObject(ORDER_KEY + orderId);
        Order order = JSON.parseObject(o.toString(), Order.class);
        //更新订单状态和服务时间
        order.setStatus(1).setStartAt(LocalDateTime.now());
        if (orderMapper.insert(order) >= 1) {
            redisTemplate.convertAndSend("order", order);
            return ApiResponse.OfStatus(Status.OK);
        }
        return ApiResponse.OfStatus(Status.ORDER_CREATE_FAILED);
    }

    @Override
    public ApiResponse finishOrder(String uuid, Long orderId) {
        Order order = new Order()
                .setEndAt(LocalDateTime.now())
                .setStatus(2);
        int update = orderMapper.update(order, Wrappers
                .lambdaUpdate(Order.class)
                .eq(Order::getOrderId, orderId));
        if (update >= 1) {
            //更新redis中的order
            redisUtil.setObject(ORDER_KEY + orderId, order);
            redisUtil.deleteObject(ORDER_KEY + "page");
            return ApiResponse.OfStatus(Status.OK);
        }
        return ApiResponse.OfStatus(Status.ORDER_EXCEPTION);
    }

    @Override
    @Cacheable(value = "orders:page", key = "#currentPage+'-'+#pageSize+'-'+#sortBy+'-'+#order")
    public ApiResponse getAllOrders(Long currentPage, Long pageSize, String sortBy, String order) {
        List<Order> orders = PageResult.GetDefaultPageList(orderMapper, new QueryWrapper<>(), currentPage, pageSize, sortBy, order);
        return ApiResponse.OfStatus(Status.OK, PageResult.Of(orders, count(), currentPage, pageSize));
    }

    @Override
    @Cacheable(value = "orders:page", key = "#uuid+'-'+#currentPage+'-'+#pageSize+'-'+#sortBy+'-'+#order")
    public ApiResponse getOrdersByUUID(String uuid, Long currentPage, Long pageSize, String sortBy, String order) {
        LambdaQueryWrapper<Order> wrapper = Wrappers.lambdaQuery(Order.class)
                .eq(Order::getCustomerUuid, uuid);
        List<Order> orders = PageResult.GetDefaultPageList(orderMapper, wrapper, currentPage, pageSize, sortBy, order);
        return ApiResponse.OfStatus(Status.OK, PageResult.Of(orders, orderMapper.selectCount(wrapper), currentPage, pageSize));
    }

    @Override
    @CacheEvict(value = "orders:page", allEntries = true)
    public ApiResponse removeOrder(String uuid, Long orderId) {
        int delete = orderMapper.delete(Wrappers.lambdaQuery(Order.class).eq(Order::getOrderId, orderId));
        if (delete >= 1) {
            redisUtil.deleteObject(ORDER_KEY + orderId);
            return ApiResponse.OfStatus(Status.OK);
        }
        return ApiResponse.OfStatus(Status.ORDER_REMOVE_FAILED);
    }
}
