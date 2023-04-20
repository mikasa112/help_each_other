package com.help.each.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.help.each.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Yuanan
 * @date 2023/4/20
 * @description
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
