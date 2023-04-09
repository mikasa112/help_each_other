package com.help.each.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.help.each.core.constant.Consts;
import com.help.each.core.constant.Status;
import com.help.each.core.dto.AddServiceRequest;
import com.help.each.core.util.RedisUtil;
import com.help.each.core.vo.ApiResponse;
import com.help.each.core.vo.PageResult;
import com.help.each.entity.Service;
import com.help.each.mapper.ServiceMapper;
import com.help.each.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationContext;

import java.util.List;


/**
 * @author Yuanan
 * @date 2023/4/5
 * @description 服务实现类
 */
@org.springframework.stereotype.Service
@RequiredArgsConstructor
//todo 2023/4/9 写的有瑕疵，代码冗余记得修改
public class ServiceServiceImpl extends ServiceImpl<ServiceMapper, Service> implements
        ServiceService {

    final ServiceMapper mapper;
    final RedisUtil redisUtil;
    //这个地方使用这个是为了使诸如@CachePut的注解生效
    final ApplicationContext applicationContext;

    private void addVisited(Long serviceId) {
        String key = Consts.SERVICE_VISITED_KEY_PREFIX + serviceId;
        Integer i = getVisited(serviceId);
        if (ObjectUtil.isNull(i)) {
            i = 0;
        }
        redisUtil.setObject(key, i + 1);
    }

    private Integer getVisited(Long serviceId) {
        String key = Consts.SERVICE_VISITED_KEY_PREFIX + serviceId;
        return redisUtil.getObject(key);
    }


    /**
     * 添加服务
     *
     * @param service {@link Service}
     */
    @Override
    @CachePut(value = "service", key = "#service.serviceId")
    public ApiResponse addService(Service service) {
        boolean b = mapper.insert(service) >= 1;
        if (!b) {
            return ApiResponse.OfStatus(Status.SERVICE_CREATE_FAILED);
        }
        return this.getService(service.getServiceId());
    }

    /**
     * 创建服务bean 服务访问次数自动加一
     *
     * @param uuid uuid
     * @param r    r
     * @return S
     */
    @Override
    public Service createService(String uuid, AddServiceRequest r) {
        long id = IdUtil.getSnowflakeNextId();
        addVisited(id);
        return new Service(id, uuid, r.getName(),
                r.getIntroduction(), r.getKeywords(), r.getPointsPrice(), r.getPictures(), r.getAddress(), 1);
    }


    /**
     * 获得服务按页数
     * 只获得id、名称、关键字、积分
     *
     * @param currentPage 当前页
     * @param pageSize    每页多大
     * @param sortBy      以什么排序
     * @param order       排序顺序默认asc
     */
    @Override
    public ApiResponse getServices(Long currentPage, Long pageSize, String sortBy, String order) {
        ServiceServiceImpl ss = applicationContext.getBean(this.getClass());
        PageResult<Service> pageResult = ss.getServicesWrap(currentPage, pageSize, sortBy, order);
        List<Service> list = pageResult.getList().stream().map(s -> s.setVisited(getVisited(s.getServiceId()))).toList();
        pageResult.setList(list);
        return ApiResponse.OfStatus(Status.OK, pageResult);
    }

    /*
    包装一下以便获得visited
     */
    @Cacheable(value = "service:page", key = "#currentPage+'-'+#pageSize+'-'+#sortBy+'-'+#order")
    public PageResult<Service> getServicesWrap(Long currentPage, Long pageSize, String sortBy, String order) {
        LambdaQueryWrapper<Service> wrapper = Wrappers.lambdaQuery(Service.class).select(Service::getServiceId, Service::getName, Service::getKeywords, Service::getPointsPrice);
        List<Service> services = PageResult.GetDefaultPageList(mapper, wrapper,
                currentPage, pageSize, sortBy, order);
        return PageResult.Of(services, count(), currentPage, pageSize);
    }

    /**
     * 查询用户自己创建的服务列表分页
     *
     * @param uuid        用户uuid
     * @param currentPage 当前页
     * @param pageSize    每页多大
     * @param sortBy      以什么排序
     * @param order       排序顺序默认asc
     */
    @Override
    public ApiResponse getServices(String uuid, Long currentPage, Long pageSize, String sortBy,
                                   String order) {
        ServiceServiceImpl ss = applicationContext.getBean(this.getClass());
        PageResult<Service> result = ss.getServicesWrap(uuid, currentPage, pageSize, sortBy, order);
        List<Service> list = result.getList().stream().map(s -> s.setVisited(getVisited(s.getServiceId()))).toList();
        result.setList(list);
        return ApiResponse.OfStatus(Status.OK, result);
    }

    /*
    包装一下，以不变应万变
     */
    @Cacheable(value = "service:page:user", key = "#uuid+'-'+#currentPage+'-'+#pageSize+'-'+#sortBy+'-'+#order")
    public PageResult<Service> getServicesWrap(String uuid, Long currentPage, Long pageSize, String sortBy,
                                               String order) {
        //这是查count的
        LambdaQueryWrapper<Service> wrapper = Wrappers.lambdaQuery(Service.class)
                .eq(Service::getUuid, uuid);
        long count = mapper.selectCount(wrapper);
        //选择查询的字段
        wrapper = wrapper.select(Service::getServiceId, Service::getName, Service::getKeywords, Service::getPointsPrice);
        List<Service> services = PageResult.GetDefaultPageList(mapper, wrapper, currentPage,
                pageSize, sortBy, order);
        List<Service> list = services.stream().map(s -> s.setVisited(getVisited(s.getServiceId()))).toList();
        return PageResult.Of(list, count, currentPage, pageSize);
    }

    /**
     * 获得单个服务详情
     *
     * @param serviceId 服务ID
     */
    @Override
    public ApiResponse getService(Long serviceId) {
        ServiceServiceImpl ss = applicationContext.getBean(this.getClass());
        Service service = ss.getServiceWrap(serviceId);
        if (ObjectUtil.isNotNull(service)) {
            addVisited(serviceId);
            service.setVisited(getVisited(serviceId));
        }
        return ApiResponse.OfStatus(Status.OK, service);
    }

    /*
    包装一下
     */
    @Cacheable(value = "service", key = "#serviceId")
    public Service getServiceWrap(Long serviceId) {
        return mapper.selectOne(Wrappers.lambdaQuery(Service.class).eq(Service::getServiceId, serviceId));
    }


    /**
     * 获取相似名字的服务列表
     *
     * @param name        服务likename
     * @param currentPage 当前页
     * @param pageSize    每页多大
     */
    @Override
    public ApiResponse getServicesName(String name, Long currentPage, Long pageSize) {
        ServiceServiceImpl ss = applicationContext.getBean(this.getClass());
        PageResult<Service> result = ss.getServicesNameWrap(name, currentPage, pageSize);
        List<Service> list = result.getList().stream().map(s -> s.setVisited(getVisited(s.getServiceId()))).toList();
        result.setList(list);
        return ApiResponse.OfStatus(Status.OK, result);
    }

    /*
    包装
     */
    @Cacheable(value = "service:page:names", key = "#name+'-'+#currentPage+'-'+#pageSize")
    public PageResult<Service> getServicesNameWrap(String name, Long currentPage, Long pageSize) {
        Page<Service> page = new Page<>();
        page.setCurrent(currentPage);
        page.setSize(pageSize);
        //这是查询count的
        LambdaQueryWrapper<Service> wrapper = Wrappers.lambdaQuery(Service.class)
                .like(Service::getName, name);
        Long count = mapper.selectCount(wrapper);
        //选择查询的字段
        wrapper = wrapper.select(Service::getServiceId, Service::getName, Service::getKeywords, Service::getPointsPrice);
        mapper.selectPage(page, wrapper);
        List<Service> services = page.getRecords();
        return PageResult.Of(services, count, currentPage, pageSize);
    }


    @Override
    @Caching(evict = {@CacheEvict(value = "service:page", allEntries = true),
            @CacheEvict(value = "service", key = "#serviceId"),
            @CacheEvict(value = "service:page:names", allEntries = true),
            @CacheEvict(value = "service:page:user", allEntries = true)})
    public ApiResponse updateService(Long serviceId, Service service) {
        boolean b = mapper.update(service,
                Wrappers.lambdaQuery(Service.class)
                        .eq(Service::getServiceId, serviceId)) >= 1;
        if (!b) {
            return ApiResponse.OfStatus(Status.SERVICE_UPDATE_FAILED);
        }
        return this.getService(serviceId);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "service", key = "#serviceId"),
            @CacheEvict(value = "service:page", allEntries = true),
            @CacheEvict(value = "service:page:names", allEntries = true),
            @CacheEvict(value = "service:page:user", allEntries = true)})
    public ApiResponse removeService(Long serviceId) {
        return ApiResponse.PrintlnApiResponse(mapper.delete(
                        Wrappers.lambdaQuery(Service.class)
                                .eq(Service::getServiceId, serviceId)) >= 1, "删除服务成功",
                Status.SERVICE_REMOVE_FAILED);
    }
}
