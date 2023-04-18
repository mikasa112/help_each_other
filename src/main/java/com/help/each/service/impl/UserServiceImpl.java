package com.help.each.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.help.each.core.constant.Status;
import com.help.each.core.vo.ApiResponse;
import com.help.each.core.vo.PageResult;
import com.help.each.entity.User;
import com.help.each.mapper.UserMapper;
import com.help.each.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;


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
    public ApiResponse list(Long currentPage, Long pageSize, String sortBy, String order) {
        List<User> users = PageResult.GetDefaultPageList(userMapper, new QueryWrapper<>(), currentPage, pageSize, sortBy, order);
        PageResult<User> pageResult = PageResult.Of(users, count(), currentPage, pageSize);
        return ApiResponse.OfStatus(Status.OK, pageResult);
    }

    @Override
    @Cacheable(value = "user", key = "#uuid")
    public ApiResponse getUserInfoByUuid(String uuid) {
        User user = userMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getUuid, uuid));
        return ApiResponse.OfSuccess(user);
    }

    @Override
    //清空缓存
    @CacheEvict(value = "user:page", allEntries = true)
    @CachePut(value = "user", key = "#uuid")
    public ApiResponse updateUserInfo(String uuid, User user) {
        if (userMapper.update(user,
                Wrappers.lambdaQuery(User.class).eq(User::getUuid, uuid)) >= 1) {
            return getUserInfoByUuid(uuid);
        }
        return ApiResponse.OfStatus(Status.USER_UPDATE_FAILED);
    }

    @Override
    @CachePut(value = "user", key = "#uuid")
    public ApiResponse removeUserByUuid(String uuid) {
        return ApiResponse.PrintlnApiResponse(userMapper
                .delete(Wrappers.lambdaQuery(User.class)
                        .eq(User::getUuid, uuid)) >= 1, "删除成功", Status.USER_REMOVE_FAILED);
    }
}
