package com.help.each.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.help.each.core.constant.Status;
import com.help.each.core.vo.ApiResponse;
import com.help.each.entity.ServiceCategory;
import com.help.each.mapper.ServiceCategoryMapper;
import com.help.each.service.ServiceCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author Yuanan
 * @date 2023/4/19
 * @description
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ServiceCategoryServiceImpl extends ServiceImpl<ServiceCategoryMapper, ServiceCategory> implements
        ServiceCategoryService {
    final ServiceCategoryMapper mapper;

    @Override
    @Cacheable(value = "categories")
    public ApiResponse getServiceCategory() {
        List<ServiceCategory> serviceCategories = mapper.selectList(new QueryWrapper<>());
        return ApiResponse.OfStatus(Status.OK, serviceCategories);
    }

    @Override
    @CacheEvict(value = "categories")
    public ApiResponse addServiceCategory(String category, String describe, String notes) {
        ServiceCategory one = mapper.selectOne(Wrappers.lambdaQuery(ServiceCategory.class).eq(ServiceCategory::getCategory, category));
        if (Objects.isNull(one)) {
            ServiceCategory serviceCategory = new ServiceCategory()
                    .setCategory(category)
                    .setDescribes(describe)
                    .setNotes(notes);
            if (mapper.insert(serviceCategory) >= 1) {
                return ApiResponse.OfStatus(Status.OK);
            }
        }
        return ApiResponse.OfStatus(Status.CATEGORY_CREATE_FAILED);
    }

    @Override
    @CacheEvict(value = "categories", allEntries = true)
    public ApiResponse updateServiceCategory(Integer id, String category, String describe, String notes) {
        ServiceCategory one = mapper.selectById(id);
        if (Objects.nonNull(one) && !one.getCategory().equals(category)) {
            ServiceCategory serviceCategory = new ServiceCategory()
                    .setCategory(category)
                    .setDescribes(describe)
                    .setNotes(notes);
            if (mapper.update(serviceCategory, Wrappers.lambdaUpdate(ServiceCategory.class).eq(ServiceCategory::getId, id)) >= 1) {
                return ApiResponse.OfStatus(Status.OK);
            }
        }
        return ApiResponse.OfStatus(Status.CATEGORY_UPDATE_FAILED);
    }

    @Override
    @CacheEvict(value = "categories")
    public ApiResponse removeServiceCategory(Integer id) {
        if (mapper.deleteById(id) >= 1) {
            return ApiResponse.OfStatus(Status.OK);
        }
        return ApiResponse.OfStatus(Status.CATEGORY_REMOVE_FAILED);
    }
}
