package com.help.each.controller.v1;

import com.help.each.core.dto.CategoryParamRequest;
import com.help.each.core.vo.ApiResponse;
import com.help.each.service.ServiceCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Yuanan
 * @date 2023/4/19
 * @description 服务分类相关接口
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("api/v1/category")
public class ServiceCategoryController {

    final ServiceCategoryService service;

    @GetMapping
    public ApiResponse index() {
        return service.getServiceCategory();
    }

    @PostMapping
    public ApiResponse put(@RequestBody @Valid CategoryParamRequest request) {
        return service.addServiceCategory(request.getCategory(), request.getDescribe(), request.getNotes());
    }

    @PutMapping("{id}")
    public ApiResponse update(@PathVariable("id") Integer id, @RequestBody @Valid CategoryParamRequest request) {
        return service.updateServiceCategory(id, request.getCategory(), request.getDescribe(), request.getNotes());
    }

    @DeleteMapping("{id}")
    public ApiResponse remove(@PathVariable("id") Integer id) {
        return service.removeServiceCategory(id);
    }
}
