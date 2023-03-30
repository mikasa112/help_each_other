package com.help.each;

import cn.hutool.core.lang.Console;
import com.help.each.core.vo.PageResult;
import com.help.each.entity.User;
import com.help.each.service.UserService;
import lombok.extern.slf4j.Slf4j;
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
public class UserTest {

    @Autowired
    UserService service;

    @Test
    public void testlist() {
        PageResult<User> list = service.list(1L, 10L, "id", "desc");
        Console.log(list.toString());
    }
}
