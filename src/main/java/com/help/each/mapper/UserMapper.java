package com.help.each.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.help.each.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Yuanan
 * @date 2023/3/23
 * @description user mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
