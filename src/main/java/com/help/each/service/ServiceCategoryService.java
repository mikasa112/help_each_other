package com.help.each.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.help.each.core.vo.ApiResponse;
import com.help.each.entity.ServiceCategory;

/**
 * @author Yuanan
 * @date 2023/4/19
 * @description 服务分类的相关服务
 */
public interface ServiceCategoryService extends IService<ServiceCategory> {


    ApiResponse getServiceCategory();

    /**
     * 添加一条分类信息
     *
     * @param category 分类名称
     * @param describe 分类描述
     * @param notes    备注
     */
    ApiResponse addServiceCategory(String category, String describe, String notes);


    /**
     * 更新服务分类的信息
     *
     * @param id       分类ID
     * @param category 分类名称
     * @param describe 分类描述
     * @param notes    备注
     */
    ApiResponse updateServiceCategory(Integer id, String category, String describe, String notes);

    /**
     * 删除一条分类
     *
     * @param id id
     */
    ApiResponse removeServiceCategory(Integer id);
}
