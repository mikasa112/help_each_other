package com.help.each.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.help.each.core.constant.Consts;
import com.help.each.core.constant.OrderStatus;
import com.help.each.core.constant.ServiceStatus;
import com.help.each.core.constant.Status;
import com.help.each.core.util.RedisUtil;
import com.help.each.core.vo.ApiResponse;
import com.help.each.core.vo.PageResult;
import com.help.each.entity.Order;
import com.help.each.mapper.OrderMapper;
import com.help.each.mapper.ServiceMapper;
import com.help.each.service.OrderService;
import com.help.each.service.PointsService;
import com.help.each.service.ServiceService;
import com.help.each.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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
    final UserService userService;
    final PointsService pointsService;
    final RedisUtil redisUtil;
    final RedisTemplate<Object, Object> redisTemplate;
    final ServiceService serviceService;
    private static final String ORDER_KEY = "orders:";

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
                .setStatus(OrderStatus.AWAIT.getCode())
                .setPay(0);
        //推送订单已接单
        redisTemplate.convertAndSend("order", order);
        //用户30分钟不确认就取消
        redisUtil.setObject(ORDER_KEY + order.getOrderId(), order, 30, TimeUnit.MINUTES);
        return ApiResponse.OfStatus(Status.OK);
    }

    @Override
    public ApiResponse confimOrder(String uuid, Long orderId) {
        Object o = redisUtil.getObject(ORDER_KEY + orderId);
        if (Objects.isNull(o)) {
            return ApiResponse.OfStatus(Status.ORDER_TIMEOUT);
        }
        Order order = JSON.parseObject(o.toString(), Order.class);
        //更新订单状态和服务时间
        order.setStatus(OrderStatus.WORKING.getCode()).setStartAt(LocalDateTime.now());
        if (orderMapper.insert(order) >= 1) {
            //推送订单以确认
            redisTemplate.convertAndSend("order", order);
            //当服务消费者确认一个服务提供者时,更新服务的状态为1（已接单）
            serviceService.updateService(getService(uuid, orderId).getServiceId()
                    , new com.help.each.entity.Service().setStatus(ServiceStatus.TAKE_SERVICE.getCode()));
            return ApiResponse.OfStatus(Status.OK);
        }
        return ApiResponse.OfStatus(Status.ORDER_CREATE_FAILED);
    }

    @Override
    public ApiResponse finishOrder(String uuid, Long orderId) {
        Order order = new Order()
                .setEndAt(LocalDateTime.now())
                .setStatus(OrderStatus.FINISH.getCode());
        int update = orderMapper.update(order, Wrappers
                .lambdaUpdate(Order.class)
                .eq(Order::getOrderId, orderId));
        if (update >= 1) {
            //更新redis中的order
            redisUtil.setObject(ORDER_KEY + orderId, order);
            redisUtil.deleteObject(ORDER_KEY + "page");
            //推送订单已完成
            redisTemplate.convertAndSend("order", order);
            //当服务提供者确认订单完成时,更新服务的状态为2（已完成）
            serviceService.updateService(getService(uuid, orderId).getServiceId()
                    , new com.help.each.entity.Service().setStatus(ServiceStatus.FINISH_SERVICE.getCode()));
            return ApiResponse.OfStatus(Status.OK);
        }
        return ApiResponse.OfStatus(Status.ORDER_EXCEPTION);
    }

    @Override
    public ApiResponse getAllOrders(Long currentPage, Long pageSize, String sortBy, String order) {
        List<Order> orders = PageResult.GetDefaultPageList(orderMapper, new QueryWrapper<>(), currentPage, pageSize, sortBy, order);
        return ApiResponse.OfStatus(Status.OK, PageResult.Of(orders, count(), currentPage, pageSize));
    }

    @Override
    public ApiResponse getOrdersByUUID(String uuid, Long currentPage, Long pageSize, String sortBy, String order) {
        LambdaQueryWrapper<Order> wrapper = Wrappers.lambdaQuery(Order.class)
                .eq(Order::getCustomerUuid, uuid);
        List<Order> orders = PageResult.GetDefaultPageList(orderMapper, wrapper, currentPage, pageSize, sortBy, order);
        return ApiResponse.OfStatus(Status.OK, PageResult.Of(orders, orderMapper.selectCount(wrapper), currentPage, pageSize));
    }

    @Override
    public ApiResponse removeOrder(String uuid, Long orderId) {
        int delete = orderMapper.delete(Wrappers.lambdaQuery(Order.class).eq(Order::getOrderId, orderId));
        if (delete >= 1) {
            redisUtil.deleteObject(ORDER_KEY + orderId);
            return ApiResponse.OfStatus(Status.OK);
        }
        return ApiResponse.OfStatus(Status.ORDER_REMOVE_FAILED);
    }

    @Override
    public ApiResponse payOrder(String uuid, Long orderId, Integer evaluate) {
        Order order = new Order();
        order.setPay(1);
        order.setEvaluate(evaluate);
        if (orderMapper.update(order, Wrappers.lambdaUpdate(Order.class).eq(Order::getOrderId, orderId)) >= 1) {
            com.help.each.entity.Service service = this.getService(uuid, orderId);
            Order o = this.getOrder(orderId);
            //给服务提供者修改积分
            if (pointsService.addPointRecord(o.getProviderUuid(), orderId, service.getPointsPrice(), Consts.SYS_ORDER_FINISH)
                    .getCode().equals(Status.OK.getCode())) {
                //删除服务提供者的Redis中的信息
                redisUtil.deleteObject("user:" + o.getProviderUuid());
                return ApiResponse.OfStatus(Status.OK);
            }
        }
        return ApiResponse.OfStatus(Status.ORDER_PAY_FAILED);
    }

    @Override
    public com.help.each.entity.Service getService(String uuid, Long orderId) {
        Order order = this.getOrder(orderId);
        return serviceMapper
                .selectOne(Wrappers
                        .lambdaQuery(com.help.each.entity.Service.class)
                        .eq(com.help.each.entity.Service::getServiceId,
                                order.getServiceId()));
    }

    @Override
    public Order getOrder(Long orderId) {
        return orderMapper.selectOne(Wrappers.lambdaQuery(Order.class).eq(Order::getOrderId, orderId));
    }
}
