package com.help.each.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.help.each.core.vo.ApiResponse;
import com.help.each.entity.Comment;


/**
 * @author Yuanan
 * @date 2023/4/20
 * @description 评论服务
 */
public interface CommentService extends IService<Comment> {

    /**
     * 添加一条评论
     *
     * @param nickname 昵称
     * @param content   评论内容
     * @param uuid      uuid
     * @param serviceId 评论的服务id
     * @param avatar 头像
     */
    ApiResponse addComment(String nickname, String content, String uuid, Long serviceId, String avatar);


    /**
     * 获取服务的全部评论
     *
     * @param serviceId   服务的id
     * @param currentPage 当前页
     * @param pageSize    总页数
     * @param sortBy      以什么字段排序
     * @param order       默认升序
     */
    ApiResponse getCommentsByServiceId(Long serviceId, Long currentPage, Long pageSize, String sortBy, String order);

    /**
     * @param id      评论ID
     * @param content 评论内容
     */
    ApiResponse updateComment(Integer id, String content);

    /**
     * 点赞
     *
     * @param id 评论ID
     */
    ApiResponse likeThis(Integer id);

    /**
     * 删除一条评论
     *
     * @param id 评论ID
     */
    ApiResponse removeComment(Integer id);
}
