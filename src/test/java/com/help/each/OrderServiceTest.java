package com.help.each;

import com.help.each.core.vo.ApiResponse;
import com.help.each.service.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Yuanan
 * @date 2023/4/14
 * @description
 */
@SpringBootTest
public class OrderServiceTest {

    @Autowired
    OrderService service;

    private static final String ADMIN_UUID = "8a0aa8b7-9c39-479a-83ff-f018a4a5e7d6";
    private static final String YUANAN_UUID = "45df5a38-43fc-4da5-8ed7-2aa521614baf";

    @Test
    public void testTakeOrder() {
        ApiResponse apiResponse = service.takeOrder(YUANAN_UUID, 1644590433496797184L);
        Assertions.assertEquals(apiResponse.getCode(), 200);
    }
}
