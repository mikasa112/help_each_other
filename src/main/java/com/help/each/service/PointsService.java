package com.help.each.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.help.each.core.vo.ApiResponse;
import com.help.each.entity.Points;

/**
 * @author Yuanan
 * @date 2023/4/18
 * @description 积分服务
 */
public interface PointsService extends IService<Points> {

    /**
     * 当用户的积分发生变化时，添加一条积分记录
     *
     * @param uuid    {@link com.help.each.entity.User} uuid
     * @param orderId {@link com.help.each.entity.Order} 订单ID
     * @param record  积分记录
     * @param remark  备注
     */
    ApiResponse addPointRecord(String uuid, Long orderId, Float record, String remark);

    /**
     * 删除积分记录
     *
     * @param id {@link Points} 积分ID
     */
    ApiResponse removePointRecord(Integer id);

    /**
     * 查询全部积分记录(admin)用户
     *
     * @param currentPage 当前页
     * @param pageSize    总数
     * @param sortBy      以什么字段排序
     * @param order       默认升序
     */
    ApiResponse getPointRecords(Long currentPage, Long pageSize, String sortBy, String order);
}
