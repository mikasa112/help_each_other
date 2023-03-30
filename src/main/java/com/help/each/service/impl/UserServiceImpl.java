package com.help.each.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.help.each.core.vo.PageResult;
import com.help.each.entity.User;
import com.help.each.mapper.UserMapper;
import com.help.each.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.help.each.core.constant.Consts.DESC_ORDER;


/**
 * @author Yuanan
 * @date 2023/3/25
 * @description 用户服务实现类
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    final UserMapper userMapper;

    @Override
    @Cacheable(value = "user:page",
            key = "T(String).valueOf(#currentPage)" +
                    ".concat('-')" +
                    ".concat(#pageSize)" +
                    ".concat('-')" +
                    ".concat(#sortBy)" +
                    ".concat('-')" +
                    ".concat(#order)")
    public PageResult<User> list(Long currentPage, Long pageSize, String sortBy, String order) {
        if (Objects.isNull(sortBy) || sortBy.isEmpty()) {
            sortBy = "id";
        }
        Page<User> page = new Page<>();
        page.setSize(pageSize);
        page.setCurrent(currentPage);
        List<OrderItem> orderItemList = switch (order) {
            case DESC_ORDER -> List.of(OrderItem.desc(sortBy));
            default -> List.of(OrderItem.asc(sortBy));
        };
        page.setOrders(orderItemList);
        userMapper.selectPage(page,new QueryWrapper<>());
        List<User> users = page.getRecords();
        return PageResult.Of(users, count(), currentPage, pageSize);
    }
}
