package com.help.each.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.help.each.core.dto.AddServiceRequest;
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
     * @param service {@link Service}
     * @return {@link ApiResponse}
     */
    ApiResponse addService(Service service);

    /**
     * 创造一个服务
     *
     * @param uuid    uuid
     * @param request r
     * @return {@link Service}
     */
    Service createService(String uuid, AddServiceRequest request);

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
    ApiResponse getServices(String uuid, Long currentPage, Long pageSize, String sortBy,
                            String order);

    /**
     * 获取前十热门服务
     *
     * @return {@link ApiResponse}
     */
    ApiResponse getHotServices();

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
     * 获得服务类型的服务列表
     *
     * @param category    服务类型ID
     * @param currentPage 当前页
     * @param pageSize    每页多大
     * @param sortBy      以什么排序
     * @param order       排序顺序默认asc
     */
    ApiResponse getSercices(Integer category, Long currentPage, Long pageSize, String sortBy, String order);

    /**
     * 根据serviceId 获取服务详情
     *
     * @param serviceId 服务ID
     * @return {@link ApiResponse}
     */
    ApiResponse getService(Long serviceId);


    /**
     * 获得服务的名称列表
     *
     * @param name        服务likename
     * @param currentPage 当前页
     * @param pageSize    每页多大
     * @param sortBy      以什么排序
     * @param order       排序顺序默认asc
     * @return {@link ApiResponse}
     */
    ApiResponse getServicesName(String name, Long currentPage, Long pageSize, String sortBy, String order);


    /**
     * 更新服务by服务ID
     *
     * @param serviceId 服务ID
     * @return {@link ApiResponse}
     */
    ApiResponse updateService(Long serviceId, Service service);

    /**
     * 删除单个服务by服务ID
     *
     * @param uuid      用户ID
     * @param serviceId 服务ID
     * @return {@link ApiResponse}
     */
    ApiResponse removeService(String uuid, Long serviceId);
}
