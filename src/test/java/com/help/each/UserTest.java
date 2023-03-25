package com.help.each;

import com.help.each.service.UserService;
import lombok.extern.slf4j.Slf4j;
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
}
