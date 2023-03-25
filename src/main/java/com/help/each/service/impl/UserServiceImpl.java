package com.help.each.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.help.each.entity.User;
import com.help.each.mapper.UserMapper;
import com.help.each.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


/**
 * @author Yuanan
 * @date 2023/3/25
 * @description 用户服务实现类
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
