package com.help.each;

import cn.hutool.core.lang.Console;
import com.help.each.config.AppConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Yuanan
 * @date 2023/3/25
 * @description
 */
@SpringBootTest
public class AppConfigTest {
    @Autowired
    AppConfig appConfig;

    @Test
    public void config() {
        Console.log(appConfig.toString());
    }
}
