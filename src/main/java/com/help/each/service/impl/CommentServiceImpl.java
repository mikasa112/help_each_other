package com.help.each.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.help.each.core.constant.Status;
import com.help.each.core.util.RedisUtil;
import com.help.each.core.vo.ApiResponse;
import com.help.each.core.vo.PageResult;
import com.help.each.entity.Comment;
import com.help.each.mapper.CommentMapper;
import com.help.each.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author Yuanan
 * @date 2023/4/20
 * @description 评论服务实现类
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    private static final String COMMENT_CACHE_KEY_PREFIX = "comments:";
    final CommentMapper commentMapper;
    final RedisUtil redisUtil;

    @Override
    public ApiResponse addComment(String nickname, String content, String uuid, Long serviceId, String avatar) {
        Comment comment = new Comment(nickname, avatar, content, uuid, serviceId, 0);
        if (commentMapper.insert(comment) >= 1) {
            return ApiResponse.OfStatus(Status.OK);
        }
        return ApiResponse.OfStatus(Status.COMMENT_CREATE_FAILED);
    }

    @Override
    public ApiResponse getCommentsByServiceId(Long serviceId, Long currentPage, Long pageSize, String sortBy, String order) {
        LambdaQueryWrapper<Comment> wrapper = Wrappers.lambdaQuery(Comment.class)
                .eq(Comment::getServiceId, serviceId);
        List<Comment> comments = PageResult.GetDefaultPageList(commentMapper, wrapper, currentPage, pageSize, sortBy, order);
        PageResult<Comment> pageResult = PageResult.Of(comments, commentMapper.selectCount(wrapper), currentPage, pageSize);
        return ApiResponse.OfStatus(Status.OK, pageResult);
    }

    @Override
    public ApiResponse updateComment(Integer id, String content) {
        Comment comment = new Comment()
                .setContent(content);
        if (commentMapper.update(comment, Wrappers.lambdaUpdate(Comment.class).eq(Comment::getId, id)) >= 1) {
            return ApiResponse.OfStatus(Status.OK);
        }
        return ApiResponse.OfStatus(Status.COMMENT_UPDATE_FAILED);
    }

    @Override
    public ApiResponse likeThis(Integer id) {
        Comment comment = new Comment();
        Comment one = commentMapper.selectOne(Wrappers.lambdaQuery(Comment.class).eq(Comment::getId, id));
        if (Objects.nonNull(one)) {
            //todo 点赞应该用redis实现
            comment.setLikeCount(one.getLikeCount() + 1);
            if (commentMapper.update(comment, Wrappers.lambdaUpdate(Comment.class).eq(Comment::getId, id)) >= 1) {
                return ApiResponse.OfStatus(Status.OK);
            }
        }
        return ApiResponse.OfStatus(Status.COMMENT_LIKE_FAILED);
    }

    @Override
    public ApiResponse removeComment(Integer id) {
        if (commentMapper.deleteById(id) >= 1) {
            return ApiResponse.OfStatus(Status.OK);
        }
        return ApiResponse.OfStatus(Status.COMMENT_REMOVE_FAILED);
    }
}
