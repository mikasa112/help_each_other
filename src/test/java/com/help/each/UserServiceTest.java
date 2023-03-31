package com.help.each;

import cn.hutool.core.lang.Console;
import com.help.each.core.dto.RegisterRequest;
import com.help.each.core.vo.PageResult;
import com.help.each.entity.User;
import com.help.each.mapper.UserMapper;
import com.help.each.service.AuthenticationService;
import com.help.each.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;


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
        PageResult<User> list;
        list = service.list(1L, 10L, "id", "desc");
        Assertions.assertTrue(Objects.nonNull(list));
    }
    @Test
    public void testUpdate()
    {
        String uuid="45df5a38-43fc-4da5-8ed7-2aa521614baf";
        User user = service.getUserInfoByUuid(uuid);
        user.setAge(10);
        Assertions.assertTrue(service.updateUserInfo(uuid, user));
    }
    @Test
    public void testRemove(){
        User user=new User();
        user.setUsername("test");
        user.setPassword("12345678");
        user.setUuid("test");
        user.setRole("admin");
        Assertions.assertTrue(userMapper.insert(user)>=1);
        boolean test = service.removeUserByUuid("test");
        Assertions.assertTrue(test);
    }
}
