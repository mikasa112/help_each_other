package com.help.each.controller.v1;

import com.help.each.core.dto.OrderParamRequest;
import com.help.each.core.dto.PageParamRequest;
import com.help.each.core.dto.ServiceParamRequest;
import com.help.each.core.vo.ApiResponse;
import com.help.each.entity.MyUserDetails;
import com.help.each.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Yuanan
 * @date 2023/4/17
 * @description
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("api/v1/order")
public class OrderController {
    final OrderService orderService;

    @GetMapping
    @Secured("admin")
    public ApiResponse index(PageParamRequest paramRequest) {
        return orderService.getAllOrders(paramRequest.getPage(), paramRequest.getSize(), paramRequest.getSort(), paramRequest.getOrder());
    }

    @GetMapping("/uuid/{uuid}")
    public ApiResponse index(@PathVariable("uuid") String uuid, PageParamRequest paramRequest) {
        return orderService.getOrdersByUUID(uuid, paramRequest.getPage(), paramRequest.getSize(), paramRequest.getSort(), paramRequest.getOrder());
    }

    @PostMapping("take")
    public ApiResponse takeOrder(Authentication authentication, @RequestBody @Valid ServiceParamRequest request) {
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        String uuid = userDetails.getUser().getUuid();
        return orderService.takeOrder(uuid, request.getServiceId());
    }

    @PostMapping("confim")
    public ApiResponse confimOrder(Authentication authentication, @RequestBody @Valid OrderParamRequest request) {
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        String uuid = userDetails.getUser().getUuid();
        return orderService.confimOrder(uuid, request.getOrderId());
    }

    @PostMapping("finish")
    public ApiResponse finishOrder(Authentication authentication, @RequestBody @Valid OrderParamRequest request) {
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        String uuid = userDetails.getUser().getUuid();
        return orderService.finishOrder(uuid, request.getOrderId());
    }

    @DeleteMapping("{orderId}")
    public ApiResponse removeOrder(Authentication authentication, @PathVariable("orderId") Long orderId) {
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        String uuid = userDetails.getUser().getUuid();
        return orderService.removeOrder(uuid, orderId);
    }
}
