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
    //fix 2023/5/13 添加查询顺序和以什么排序
    public ApiResponse indexName(@PathVariable("name") String name, PageParamRequest request) {
        return service.getServicesName(name, request.getPage(), request.getSize(),request.getSort(),request.getOrder());
    }

    @PutMapping("{serviceId}")
    public ApiResponse update(@PathVariable("serviceId") Long serviceId, @Valid @RequestBody AddServiceRequest request) {
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
    public ApiResponse remove(Authentication authentication, @PathVariable("serviceId") Long serviceId) {
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        String uuid = userDetails.getUser().getUuid();
        return service.removeService(uuid, serviceId);
    }

    @PostMapping
    public ApiResponse addService(Authentication authentication, @Valid @RequestBody AddServiceRequest request) {
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        String uuid = userDetails.getUser().getUuid();
        Service s = service.createService(uuid, request);
        return service.addService(s);
    }

    @GetMapping("/category/{id}")
    public ApiResponse indexCategory(@PathVariable("id") Integer id, PageParamRequest request) {
        return service.getSercices(id, request.getPage(), request.getSize(), request.getSort(), request.getOrder());
    }

}
