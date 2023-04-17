package com.help.each.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.help.each.core.vo.ApiResponse;
import com.help.each.entity.Newsletter;

/**
 * @author Yuanan
 * @date 2023/4/17
 * @description
 */
public interface NewsletterService extends IService<Newsletter> {

    /**
     * 按页获取养老简讯
     *
     * @param currentPage 当前页
     * @param pageSize    一共多少页
     * @param sortBy      按什么排序
     * @param order       升序还是降序
     */
    ApiResponse getNewsletter(Long currentPage, Long pageSize, String sortBy, String order);

    /**
     * 添加一个简讯
     *
     * @param content 内容
     */
    ApiResponse putNewsletter(String content);


    /**
     * 修改简讯
     *
     * @param id      id
     * @param content 内容
     */
    ApiResponse updateNewsletter(Integer id, String content);

    /**
     * 删除简讯
     *
     * @param id id
     */
    ApiResponse removeNewsletter(Integer id);
}
