package com.help.each.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.help.each.core.constant.Status;
import com.help.each.core.vo.ApiResponse;
import com.help.each.core.vo.PageResult;
import com.help.each.entity.Service;
import com.help.each.mapper.ServiceMapper;
import com.help.each.service.ServiceService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;


/**
 * @author Yuanan
 * @date 2023/4/5
 * @description 服务实现类
 */
@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceServiceImpl extends ServiceImpl<ServiceMapper, Service> implements
    ServiceService {

    final ServiceMapper mapper;


    @Override
    @CachePut(value = "service:user", key = "#uuid" + ".concat('-')"
        + ".concat(#service.getServiceId())")
    public ApiResponse addService(String uuid, Service service) {
        boolean b = mapper.insert(service) >= 1;
        if (!b) {
            return ApiResponse.OfStatus(Status.SERVICE_CREATE_FAILED);
        }
        return this.getServices(uuid, service.getServiceId());
    }

    @Override
    @Cacheable(value = "service:page", key = "T(String).valueOf(#currentPage)" + ".concat('-')"
        + ".concat(#pageSize)" + ".concat('-')" + ".concat(#sortBy)" + ".concat('-')"
        + ".concat(#order)")
    public ApiResponse getServices(Long currentPage, Long pageSize, String sortBy, String order) {
        List<Service> services = PageResult.GetDefaultPageList(mapper, new QueryWrapper<>(),
            currentPage, pageSize, sortBy, order);
        PageResult<Service> pageResult = PageResult.Of(services, count(), currentPage, pageSize);
        return ApiResponse.OfStatus(Status.OK, pageResult);
    }

    @Override
    @Cacheable(value = "service:page:user", key = "#uuid" + ".concat('-')" + ".concat(#currentPage)"
        + ".concat('-')" + ".concat(#pageSize)" + ".concat('-')" + ".concat(#sortBy)"
        + ".concat('-')" + ".concat(#order)")
    public ApiResponse getServices(String uuid, Long currentPage, Long pageSize, String sortBy,
        String order) {
        LambdaQueryWrapper<Service> wrapper = Wrappers.lambdaQuery(Service.class)
            .eq(Service::getUuid, uuid);
        List<Service> services = PageResult.GetDefaultPageList(mapper, wrapper, currentPage,
            pageSize, sortBy, order);
        PageResult<Service> result = PageResult.Of(services, count(wrapper), currentPage, pageSize);
        return ApiResponse.OfStatus(Status.OK, result);
    }

    @Override
    @Cacheable(value = "service:user", key = "#uuid" + ".concat('-')" + ".concat(#name)")
    public ApiResponse getServices(String uuid, String name) {
        Service service = mapper.selectOne(
            Wrappers.lambdaQuery(Service.class).eq(Service::getUuid, uuid)
                .like(Service::getName, name));
        return ApiResponse.OfStatus(Status.OK, service);
    }

    @Override
    @Cacheable(value = "service:user", key = "#uuid" + ".concat('-')" + ".concat(#serviceId)")
    public ApiResponse getServices(String uuid, Long serviceId) {
        Service service = mapper.selectOne(
            Wrappers.lambdaQuery(Service.class).eq(Service::getUuid, uuid)
                .eq(Service::getServiceId, serviceId));
        return ApiResponse.OfStatus(Status.OK, service);
    }

    @Override
    @CachePut(value = "service:page:user", key = "#uuid" + ".concat('-')" + ".concat(#serviceId)")
    public ApiResponse updateService(String uuid, Long serviceId, Service service) {
        boolean b = mapper.update(service,
            Wrappers.lambdaQuery(Service.class).eq(Service::getUuid, uuid)
                .eq(Service::getServiceId, serviceId)) >= 1;
        if (!b) {
            return ApiResponse.OfStatus(Status.SERVICE_UPDATE_FAILED);
        } return this.getServices(uuid, serviceId);
    }

    @Override
    @Caching(evict = {
        @CacheEvict(value = "service:user", key = "#uuid" + ".concat('-')" + ".concat(#serviceId)"),
        @CacheEvict(value = "service:page:user", allEntries = true),
        @CacheEvict(value = "service:page", allEntries = true)})
    public ApiResponse removeService(String uuid, Long serviceId) {
        return ApiResponse.PrintlnApiResponse(mapper.delete(
                Wrappers.lambdaQuery(Service.class).eq(Service::getUuid, uuid)
                    .eq(Service::getServiceId, serviceId)) >= 1, "删除服务成功",
            Status.SERVICE_REMOVE_FAILED);
    }
}
