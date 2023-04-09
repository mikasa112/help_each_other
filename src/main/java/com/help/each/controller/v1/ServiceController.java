package com.help.each.controller.v1;

import com.help.each.core.dto.AddServiceRequest;
import com.help.each.core.dto.PageParamDefaultRequest;
import com.help.each.core.dto.PageParamRequest;
import com.help.each.core.vo.ApiResponse;
import com.help.each.entity.MyUserDetails;
import com.help.each.entity.Service;
import com.help.each.service.ServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @author Yuanan
 * @date 2023/4/8
 * @description 服务接口
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("api/v1/service")
public class ServiceController {
    final ServiceService service;

    /*
    服务的全部接口(分页)
     */
    @GetMapping
    public ApiResponse index(PageParamRequest paramRequest) {
        return service.getServices(paramRequest.getPage(), paramRequest.getSize(), paramRequest.getSort(), paramRequest.getOrder());
    }

    @GetMapping("/uuid/{uuid}")
    public ApiResponse index(@PathVariable("uuid") String uuid, PageParamRequest paramRequest) {
        return service.getServices(uuid, paramRequest.getPage(), paramRequest.getSize(), paramRequest.getSort(), paramRequest.getOrder());
    }

    @GetMapping("{serviceId}")
    public ApiResponse index(@PathVariable("serviceId") Long serviceId) {
        return service.getService(serviceId);
    }

    @GetMapping("/name/{name}")
    public ApiResponse index(@PathVariable("name") String name, PageParamDefaultRequest request) {
        return service.getServicesName(name, request.getPage(), request.getSize());
    }

    @PutMapping("{serviceId}")
    public ApiResponse update(@PathVariable("serviceId") Long serviceId, @Valid AddServiceRequest request) {
        Service s = new Service();
        s.setName(request.getName())
                .setAddress(request.getAddress())
                .setIntroduction(request.getIntroduction())
                .setKeywords(request.getKeywords())
                .setPictures(request.getPictures())
                .setPointsPrice(request.getPointsPrice());
        service.updateService(serviceId, s);
        return service.updateService(serviceId, s);
    }

    @DeleteMapping("{serviceId}")
    public ApiResponse remove(@PathVariable("serviceId") Long serviceId) {
        return service.removeService(serviceId);
    }

    @PostMapping
    public ApiResponse addService(Authentication authentication, @Valid AddServiceRequest request) {
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        String uuid = userDetails.getUser().getUuid();
        Service s = service.createService(uuid, request);
        return service.addService(s);
    }

}
