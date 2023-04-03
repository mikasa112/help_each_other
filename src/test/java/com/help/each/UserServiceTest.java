package com.help.each;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.help.each.core.vo.ApiResponse;
import com.help.each.entity.User;
import com.help.each.mapper.UserMapper;
import com.help.each.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Yuanan
 * @date 2023/3/23
 * @description
 */
@SpringBootTest
@Slf4j
public class UserServiceTest {

    @Autowired
    UserService service;
    @Autowired
    UserMapper userMapper;

    @Test
    public void testlist() {
        ApiResponse apiResponse = service.list(1L, 10L, "id", "desc");
        Assertions.assertEquals(apiResponse.getCode(), 200);
    }

    @Test
    public void testUpdate() {
        String uuid = "45df5a38-43fc-4da5-8ed7-2aa521614baf";
        User user = userMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getUuid, uuid));
        user.setAge(10);
        ApiResponse apiResponse = service.updateUserInfo(uuid, user);
        Assertions.assertEquals(apiResponse.getCode(), 200);
    }

    @Test
    public void testRemove() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("12345678");
        user.setUuid("test");
        user.setRole("admin");
        Assertions.assertTrue(userMapper.insert(user) >= 1);
        ApiResponse apiResponse = service.removeUserByUuid("test");
        Assertions.assertEquals(apiResponse.getCode(), 200);
    }
}
