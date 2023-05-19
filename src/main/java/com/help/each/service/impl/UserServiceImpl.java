package com.help.each.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.help.each.core.constant.OrderStatus;
import com.help.each.core.constant.Status;
import com.help.each.core.vo.ApiResponse;
import com.help.each.core.vo.PageResult;
import com.help.each.core.vo.UserInfo;
import com.help.each.entity.MyUserDetails;
import com.help.each.entity.Order;
import com.help.each.entity.User;
import com.help.each.mapper.OrderMapper;
import com.help.each.mapper.ServiceMapper;
import com.help.each.mapper.UserMapper;
import com.help.each.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author Yuanan
 * @date 2023/3/25
 * @description 用户服务实现类
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    final UserMapper userMapper;
    final ServiceMapper serviceMapper;
    final OrderMapper orderMapper;
    final PasswordEncoder passwordEncoder;

    @Override
    public ApiResponse list(Long currentPage, Long pageSize, String sortBy, String order) {
        List<User> users = PageResult.GetDefaultPageList(userMapper, new QueryWrapper<>(), currentPage, pageSize, sortBy, order);
        PageResult<User> pageResult = PageResult.Of(users, count(), currentPage, pageSize);
        return ApiResponse.OfStatus(Status.OK, pageResult);
    }

    @Override
    @Cacheable(value = "user", key = "#uuid")
    public ApiResponse getUserInfoByUuid(String uuid) {
        UserInfo userInfo = new UserInfo();
        //获取uuid的全部信息 2023/4/24
        User user = userMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getUuid, uuid));
        userInfo.setUser(user);
        List<Order> orders = this.getOrdersByProvider(uuid);
        List<com.help.each.entity.Service> list = new ArrayList<>();
        AtomicInteger sum = new AtomicInteger();
        AtomicInteger count = new AtomicInteger();
        orders.forEach(e -> {
            count.incrementAndGet();
            com.help.each.entity.Service service = getService(e.getServiceId());
            list.add(service);
            //获得评价
            Integer evaluate = e.getEvaluate();
            if (Objects.nonNull(evaluate)) {
                sum.addAndGet(evaluate);
            }
        });
        int value = 0;
        if (sum.get() != 0 && count.get() != 0) {
            value = sum.get() / count.get();
        }
        userInfo.setServices(list);
        userInfo.setEvaluate(value);
        return ApiResponse.OfSuccess(userInfo);
    }

    @Override
    public ApiResponse getUserInfoByName(String name) {
        User user = userMapper.selectOne(Wrappers.lambdaQuery(User.class)
                .eq(User::getUsername, name));
        return ApiResponse.OfStatus(Status.OK, user);
    }

    @Override
    //清空缓存
    @Caching(put = @CachePut(value = "user", key = "#uuid"))
    public ApiResponse updateUserInfo(String uuid, User user) {
        //将用户的密码加密
        if (Objects.nonNull(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if (userMapper.update(user,
                Wrappers.lambdaQuery(User.class).eq(User::getUuid, uuid)) >= 1) {
            return getUserInfoByUuid(uuid);
        }
        return ApiResponse.OfStatus(Status.USER_UPDATE_FAILED);
    }

    @Override
    @Caching(evict = @CacheEvict(value = "user", key = "#uuid"))
    public ApiResponse removeUserByUuid(String uuid) {
        return ApiResponse.PrintlnApiResponse(userMapper
                .delete(Wrappers.lambdaQuery(User.class)
                        .eq(User::getUuid, uuid)) >= 1, "删除成功", Status.USER_REMOVE_FAILED);
    }

    private List<Order> getOrdersByProvider(String uuid) {
        LambdaQueryWrapper<Order> wrapper = Wrappers.lambdaQuery(Order.class)
                .eq(Order::getProviderUuid, uuid)
                .eq(Order::getStatus, OrderStatus.FINISH.getCode())
                .select(Order::getServiceId, Order::getEvaluate);
        return orderMapper.selectList(wrapper);
    }

    private com.help.each.entity.Service getService(Long serviceId) {
        return serviceMapper.selectOne(Wrappers.lambdaQuery(com.help.each.entity.Service.class).eq(
                com.help.each.entity.Service::getServiceId, serviceId).select(com.help.each.entity.Service::getName
                , com.help.each.entity.Service::getServiceId));
    }
}
