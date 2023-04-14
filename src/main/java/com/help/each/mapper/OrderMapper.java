package com.help.each.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.help.each.entity.Order;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Yuanan
 * @date 2023/4/14
 * @description
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
