package com.help.each.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.help.each.core.vo.ApiResponse;
import com.help.each.entity.Service;

/**
 * @author Yuanan
 * @date 2023/4/5
 * @description {@link Service} 的服务接口
 */
public interface ServiceService extends IService<Service> {

    /**
     * 添加一个服务
     *
     * @param uuid    用户的UUID
     * @param service {@link Service}
     * @return {@link ApiResponse}
     */
    ApiResponse addService(String uuid, Service service);

    /**
     * 获取服务列表以uuid
     *
     * @param uuid        用户uuid
     * @param currentPage 当前页
     * @param pageSize    每页多大
     * @param sortBy      以什么排序
     * @param order       排序顺序默认asc
     * @return {@link ApiResponse}
     */
    ApiResponse getServices(String uuid, Long currentPage, Long pageSize, String sortBy, String order);

    /**
     * 获取服务列表
     *
     * @param currentPage 当前页
     * @param pageSize    每页多大
     * @param sortBy      以什么排序
     * @param order       排序顺序默认asc
     * @return {@link ApiResponse}
     */
    ApiResponse getServices(Long currentPage, Long pageSize, String sortBy, String order);

    /**
     * 获取单个服务从UUID和服务名字
     *
     * @param uuid 用户UUID
     * @param name 服务名字
     * @return {@link ApiResponse}
     */
    ApiResponse getServices(String uuid, String name);

    /**
     * 获取单个服务从UUID和服务ID
     *
     * @param uuid      用户ID
     * @param serviceId 服务ID
     * @return {@link ApiResponse}
     */
    ApiResponse getServices(String uuid, Long serviceId);

    /**
     * 更新服务从UUID和服务ID
     *
     * @param uuid      用户ID
     * @param serviceId 服务ID
     * @return {@link ApiResponse}
     */
    ApiResponse updateService(String uuid, Long serviceId);

    /**
     * 删除单个服务从UUID和服务ID
     *
     * @param uuid      用户ID
     * @param serviceId 服务ID
     * @return {@link ApiResponse}
     */
    ApiResponse removeService(String uuid, Long serviceId);
}
