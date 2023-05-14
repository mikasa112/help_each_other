package com.help.each;

import cn.hutool.core.util.IdUtil;
import com.help.each.core.constant.Consts;
import com.help.each.core.vo.ApiResponse;
import com.help.each.entity.Service;
import com.help.each.mapper.ServiceMapper;
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
    private static final String ADMIN_UUID = "8a0aa8b7-9c39-479a-83ff-f018a4a5e7d6";
    private static final String YUANAN_UUID = "45df5a38-43fc-4da5-8ed7-2aa521614baf";

    @Autowired
    ServiceService service;

    @Autowired
    ServiceMapper mapper;

    @Test
    public void testInsert() {
        Service s = new Service();
        s.setUuid(ADMIN_UUID).setServiceId(IdUtil.getSnowflakeNextId()).setName("这个是测试服务").setIntroduction("这是测试这是测试这是测试这是测试这是测试这是测试").setKeywords("这是测试").setPointsPrice(10F);
        ApiResponse apiResponse = service.addService(s);
        Assertions.assertEquals(apiResponse.getCode(), 200);
    }

    @Test
    public void testPage() {
        ApiResponse apiResponse1 = service.getServices(ADMIN_UUID, 1L, Consts.DEFAULT_PAGE_SIZE, Consts.SORT_BY, Consts.ASC_ORDER);
        Assertions.assertEquals(apiResponse1.getCode(), 200);
        ApiResponse apiResponse2 = service.getServices(YUANAN_UUID, 1L, Consts.DEFAULT_PAGE_SIZE, Consts.SORT_BY, Consts.ASC_ORDER);
        Assertions.assertEquals(apiResponse2.getCode(), 200);
    }

    @Test
    public void testSelectByUUid() {
        ApiResponse s1 = service.getService(1643629158876917760L);
        Assertions.assertEquals(s1.getCode(), 200);
        ApiResponse s2 = service.getService(1644563071778496512L);
        Assertions.assertEquals(s2.getCode(), 200);
    }

    @Test
    public void testUpdate() {
        Service s = new Service();
        s.setName("test test");
        ApiResponse api = service.updateService(1643629158876917760L, s);
        Assertions.assertEquals(api.getCode(), 200);
    }

    @Test
    public void testDelete() {
        ApiResponse api = service.removeService(ADMIN_UUID, 1644563071778496512L);
        Assertions.assertEquals(api.getCode(), 200);
    }

    @Test
    public void testSelectLikeName() {
//        ApiResponse api = service.getServicesName("测", 1L, Consts.DEFAULT_PAGE_SIZE);
//        Assertions.assertEquals(api.getCode(), 200);
    }
}
