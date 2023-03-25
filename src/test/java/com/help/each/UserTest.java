package com.help.each;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.help.each.entity.User;
import com.help.each.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author Yuanan
 * @date 2023/3/23
 * @description
 */
@SpringBootTest
@Slf4j
public class UserTest {
    @Autowired
    UserMapper userMapper;

    public void add() {
        User user = new User();
        user.setUuid(IdUtil.randomUUID());
        user.setUsername("yuanan");
        user.setPassword("admin");
        user.setRole("user");
        userMapper.insert(user);
    }

    @Test
    public void get() {
        List<User> users = userMapper.selectList(new QueryWrapper<>());
        users.forEach(System.out::println);
    }
}
