package com.help.each;

import cn.hutool.core.util.IdUtil;
import com.help.each.config.AppConfig;
import com.help.each.core.constant.Consts;
import com.help.each.core.vo.ApiResponse;
import com.help.each.entity.Service;
import com.help.each.service.ServiceService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Yuanan
 * @date 2023/4/5
 * @description 服务的服务测试
 */
@SpringBootTest
@Slf4j
public class ServiceServiceTest {
    private static final String UUID = "45df5a38-43fc-4da5-8ed7-2aa521614baf";
    @Autowired
    ServiceService service;
    @Autowired
    AppConfig appConfig;

    @Test
    public void testInsert() {
        Service s = new Service();
        s.setUuid(UUID)
                .setServiceId(IdUtil.getSnowflakeNextId())
                .setName("这个是测试服务")
                .setIntroduction("这是测试这是测试这是测试这是测试这是测试这是测试")
                .setKeywords("这是测试")
                .setPointsPrice(20D);
        ApiResponse apiResponse = service.addService(UUID, s);
        Assertions.assertEquals(apiResponse.getCode(), 200);
    }

    @Test
    public void testSelectByUUID() {
        ApiResponse apiResponse = service.getServices(UUID, 1L, appConfig.getPageSize(), Consts.SORT_BY, Consts.ASC_ORDER);
        Assertions.assertEquals(apiResponse.getCode(), 200);
    }
}
