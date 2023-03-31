package com.help.each.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.help.each.core.vo.PageResult;
import com.help.each.entity.User;



/**
 * @author Yuanan
 * @date 2023/3/23
 * @description 用户服务定义
 */
public interface UserService extends IService<User> {
    /**
     * @param currentPage 当前页
     * @param pageSize    每页多大
     * @param sortBy      以什么排序
     * @param order       排序顺序
     * @return 分页 {@link PageResult}
     */
    PageResult<User> list(Long currentPage, Long pageSize, String sortBy, String order);

    /**
     * 获取用户的信息从uuid
     * @param uuid
     * @return
     */
    User getUserInfoByUuid(String uuid);

    /**
     * 更新uuid的用户信息
     * @param uuid uuid
     * @param user {@link User}
     * @return bool
     */
    boolean updateUserInfo(String uuid,User user);

    /**
     * 删除用户byUUID
     * @param uuid uuid
     * @return bool
     */
    boolean removeUserByUuid(String uuid);
}
