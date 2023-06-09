package com.help.each.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.help.each.core.constant.Consts;
import com.help.each.core.constant.Role;
import com.help.each.core.constant.ServiceStatus;
import com.help.each.core.constant.Status;
import com.help.each.core.dto.AddServiceRequest;
import com.help.each.core.exception.BaseException;
import com.help.each.core.util.RedisUtil;
import com.help.each.core.util.Util;
import com.help.each.core.vo.ApiResponse;
import com.help.each.core.vo.PageResult;
import com.help.each.core.vo.UserInfo;
import com.help.each.entity.Service;
import com.help.each.entity.User;
import com.help.each.mapper.ServiceMapper;
import com.help.each.service.PointsService;
import com.help.each.service.ServiceService;
import com.help.each.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;


/**
 * @author Yuanan
 * @date 2023/4/5
 * @description 服务实现类
 */
@org.springframework.stereotype.Service
@Slf4j
@RequiredArgsConstructor
//todo 2023/4/9 写的有瑕疵，代码冗余记得修改
public class ServiceServiceImpl extends ServiceImpl<ServiceMapper, Service> implements
        ServiceService {

    final ServiceMapper mapper;
    final RedisUtil redisUtil;
    final UserService userService;
    final PointsService pointsService;
    //这个地方使用这个是为了使诸如@CachePut的注解生效
    final ApplicationContext applicationContext;

    private static final String SERVICE_KEY = "service";

    private void addVisited(Service service) {
//        String key = Consts.SERVICE_VISITED_KEY_PREFIX + serviceId;
//        Integer i = getVisited(serviceId);
//        if (ObjectUtil.isNull(i)) {
//            i = 0;
//        }
//        redisUtil.setObject(key, i + 1);
        redisUtil.incrementScore(SERVICE_KEY, service, 1L);
    }

    private Double getVisited(Service service) {
        return redisUtil.getScoreFromValue(SERVICE_KEY, service);
//        String key = Consts.SERVICE_VISITED_KEY_PREFIX + serviceId;
//        return redisUtil.getObject(key);
    }


    /**
     * 添加服务
     *
     * @param service {@link Service}
     */
    @Override
    public ApiResponse addService(Service service) {
        boolean b = mapper.insert(service) >= 1;
        if (!b) {
            return ApiResponse.OfStatus(Status.SERVICE_CREATE_FAILED);
        }
        //如果是常规用户才操作积分
        UserInfo userInfo = (UserInfo) userService.getUserInfoByUuid(service.getUuid()).getData();
        if (Role.USER.equals(userInfo.getUser().getRole())) {
            //当服务消费者创建服务成功时，系统先将积分扣除
            pointsService.addPointRecord(service.getUuid(), null, Util.NegativeNum(service.getPointsPrice()), Consts.SYS_POINT_REMARK_SUB);
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
        //fix 2023/5/12 更新获取用户信息后没有修改对应的类型转换，转为UserInfo
        UserInfo userInfo = (UserInfo) userService.getUserInfoByUuid(uuid).getData();
        //如果用户的积分不够，则创建失败
        if (Objects.isNull(r.getPointsPrice())
                || Objects.isNull(userInfo.getUser().getPoints())
                || r.getPointsPrice() > userInfo.getUser().getPoints()) {
            throw new BaseException(Status.USER_POINTS_NOT_ENOUGH);
        }
        long id = IdUtil.getSnowflakeNextId();
        return new Service(id, uuid, r.getName(),
                r.getIntroduction(), r.getKeywords(), r.getPointsPrice(), r.getPictures(), r.getAddress(),
                1D, ServiceStatus.NOT_TAKE_SERVICE.getCode(), r.getLongitude(), r.getLatitude(), r.getCategory());
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
        List<Service> list = pageResult.getList().stream().map(s -> s.setVisited(getVisited(s))).toList();
        pageResult.setList(list);
        return ApiResponse.OfStatus(Status.OK, pageResult);
    }

    /**
     * 获得服务按服务分类和页数
     *
     * @param category    服务类型ID
     * @param currentPage 当前页
     * @param pageSize    每页多大
     * @param sortBy      以什么排序
     * @param order       排序顺序默认asc
     */
    @Override
    public ApiResponse getSercices(Integer category, Long currentPage, Long pageSize, String sortBy, String order) {
        ServiceServiceImpl ss = applicationContext.getBean(this.getClass());
        PageResult<Service> pageResult = ss.getSercicesWrap(category, currentPage, pageSize, sortBy, order);
        List<Service> list = pageResult.getList().stream().map(s -> s.setVisited(getVisited(s))).toList();
        pageResult.setList(list);
        return ApiResponse.OfStatus(Status.OK, pageResult);
    }


    /*
     包装一下以便获得visited
     */
    public PageResult<Service> getSercicesWrap(Integer category, Long currentPage, Long pageSize, String sortBy, String order) {
        LambdaQueryWrapper<Service> wrapper = Wrappers.lambdaQuery(Service.class)
                .eq(Service::getCategory, category);
        Long count = mapper.selectCount(wrapper);
        List<Service> services = PageResult.GetDefaultPageList(mapper, wrapper,
                currentPage, pageSize, sortBy, order);
        //fix 修改这个获取到全部长度的bug
        return PageResult.Of(services, count, currentPage, pageSize);
    }


    /*
    包装一下以便获得visited
     */
    public PageResult<Service> getServicesWrap(Long currentPage, Long pageSize, String sortBy, String order) {
        LambdaQueryWrapper<Service> wrapper = Wrappers.lambdaQuery(Service.class);
//                .select(Service::getServiceId, Service::getName, Service::getKeywords, Service::getPointsPrice, Service::getStatus, Service::getCategory, Service::getPictures);
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
        List<Service> list = result.getList().stream().map(s -> s.setVisited(getVisited(s))).toList();
        result.setList(list);
        return ApiResponse.OfStatus(Status.OK, result);
    }

    @Override
    public ApiResponse getHotServices() {
        //获取前十个热门元素
        Set<Object> set = redisUtil.rangeFromZSet(SERVICE_KEY, 0, 4);
        List<Service> services = new ArrayList<>();
        set.forEach(s -> {
            Service temp = JSON.parseObject(s.toString(), Service.class);
            services.add(temp);
        });
        List<Service> list = services.stream().map(s -> s.setVisited(getVisited(s))).toList();
        return ApiResponse.OfStatus(Status.OK, list);
    }

    /*
    包装一下，以不变应万变
     */
    public PageResult<Service> getServicesWrap(String uuid, Long currentPage, Long pageSize, String sortBy,
                                               String order) {
        //这是查count的
        LambdaQueryWrapper<Service> wrapper = Wrappers.lambdaQuery(Service.class)
                .eq(Service::getUuid, uuid);
        long count = mapper.selectCount(wrapper);
        //选择查询的字段
//        wrapper = wrapper.select(Service::getServiceId, Service::getName, Service::getKeywords, Service::getPointsPrice, Service::getStatus);
        List<Service> services = PageResult.GetDefaultPageList(mapper, wrapper, currentPage,
                pageSize, sortBy, order);
        List<Service> list = services.stream().map(s -> s.setVisited(getVisited(s))).toList();
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
            addVisited(service);
            service.setVisited(getVisited(service));
        }
        return ApiResponse.OfStatus(Status.OK, service);
    }

    /*
    包装一下
     */
//    @Cacheable(value = "service", key = "#serviceId")
    //2023/5/17 修改为set存储service
    public Service getServiceWrap(Long serviceId) {
        Set<Object> set = redisUtil.rangeFromZSet(SERVICE_KEY, 0, -1);
        AtomicReference<Service> service = new AtomicReference<>();
        set.forEach(s -> {
            Service temp = JSON.parseObject(s.toString(), Service.class);
            if (temp.getServiceId().equals(serviceId))
                service.set(temp);
        });
        if (Objects.isNull(service.get())) {
            Service selected = mapper.selectOne(Wrappers.lambdaQuery(Service.class).eq(Service::getServiceId, serviceId));
            if (!Objects.isNull(selected)) {
                redisUtil.insertValueFromZSet(SERVICE_KEY, selected, 1D);
                service.set(selected);
            }
        }
        return service.get();
    }

    /**
     * 获取相似名字的服务列表
     *
     * @param name        服务likename
     * @param currentPage 当前页
     * @param pageSize    每页多大
     * @param sortBy      以什么排序
     * @param order       排序顺序默认asc
     */
    @Override
    public ApiResponse getServicesName(String name, Long currentPage, Long pageSize, String sortBy, String order) {
        ServiceServiceImpl ss = applicationContext.getBean(this.getClass());
        PageResult<Service> result = ss.getServicesNameWrap(name, currentPage, pageSize, sortBy, order);
        List<Service> list = result.getList().stream().map(s -> s.setVisited(getVisited(s))).toList();
        result.setList(list);
        return ApiResponse.OfStatus(Status.OK, result);
    }

    /*
    包装
     */
    public PageResult<Service> getServicesNameWrap(String name, Long currentPage, Long pageSize, String sortBy, String order) {
//        Page<Service> page = new Page<>();
//        page.setCurrent(currentPage);
//        page.setSize(pageSize);
        //这是查询count的
        LambdaQueryWrapper<Service> wrapper = Wrappers.lambdaQuery(Service.class)
                .like(Service::getName, name);
        Long count = mapper.selectCount(wrapper);
        //选择查询的字段 fix:2023/5/13 添加查询字段图片
//        wrapper = wrapper.select(Service::getServiceId, Service::getName, Service::getKeywords, Service::getPointsPrice, Service::getStatus, Service::getCategory, Service::getPictures);
        //fix 2023/5/13添加规则查询
        List<Service> services = PageResult.GetDefaultPageList(mapper, wrapper, currentPage, pageSize, sortBy, order);
//        mapper.selectPage(page, wrapper);
        return PageResult.Of(services, count, currentPage, pageSize);
    }


    @Override
    //fix 2023/5/17 修改手动删除redis
//    @Caching(evict = @CacheEvict(value = "service", key = "#serviceId"))
    public ApiResponse updateService(Long serviceId, Service service) {
        boolean b = mapper.update(service,
                Wrappers.lambdaQuery(Service.class)
                        .eq(Service::getServiceId, serviceId)) >= 1;
        if (!b) {
            return ApiResponse.OfStatus(Status.SERVICE_UPDATE_FAILED);
        }
        redisUtil.removeValueFromZSet(SERVICE_KEY, this.getServiceWrap(serviceId));
        return this.getService(serviceId);
    }

    //fix 2023/5/17 修改手动删除redis
    @Override
    @Caching(evict = {
//            @CacheEvict(value = "service", key = "#serviceId"),
            @CacheEvict(value = "service:visited_count", key = "#serviceId")})
    public ApiResponse removeService(String uuid, Long serviceId) {
        if (mapper.delete(
                Wrappers.lambdaQuery(Service.class)
                        .eq(Service::getServiceId, serviceId)) >= 1) {
            User user = (User) userService.getUserInfoByUuid(uuid).getData();
            //如果是常规用户才操作积分
            if (Role.USER.equals(user.getRole())) {
                ServiceServiceImpl serviceImpl = applicationContext.getBean(this.getClass());
                Service service = serviceImpl.getServiceWrap(serviceId);
                redisUtil.removeValueFromZSet(SERVICE_KEY, service);
                //当服务消费者取消/删除服务成功时，系统先将积分还给用户
                pointsService.addPointRecord(uuid, null, service.getPointsPrice(), Consts.SYS_POINT_REMARK_RETURN);
            }
            return ApiResponse.OfStatus(Status.OK);
        }
        return ApiResponse.OfStatus(Status.SERVICE_REMOVE_FAILED);
    }
}
