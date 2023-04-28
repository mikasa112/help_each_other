package com.help.each.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.help.each.core.constant.Status;
import com.help.each.core.vo.ApiResponse;
import com.help.each.core.vo.PageResult;
import com.help.each.entity.Newsletter;
import com.help.each.mapper.NewsletterMapper;
import com.help.each.service.NewsletterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Yuanan
 * @date 2023/4/17
 * @description
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class NewsletterServiceImpl extends ServiceImpl<NewsletterMapper, Newsletter> implements NewsletterService {
    final NewsletterMapper mapper;


    @Override
    public ApiResponse getNewsletter(Long currentPage, Long pageSize, String sortBy, String order) {
        List<Newsletter> newsletters = PageResult.GetDefaultPageList(mapper, new QueryWrapper<>(), currentPage, pageSize, sortBy, order);
        PageResult<Newsletter> result = PageResult.Of(newsletters, count(), currentPage, pageSize);
        return ApiResponse.OfStatus(Status.OK, result);
    }

    @Override
    public ApiResponse putNewsletter(String content) {
        Newsletter newsletter = new Newsletter(content);
        if (mapper.insert(newsletter) >= 1) {
            return ApiResponse.OfStatus(Status.OK);
        }
        return ApiResponse.OfStatus(Status.ERROR);
    }

    @Override
    public ApiResponse updateNewsletter(Integer id, String content) {
        Newsletter newsletter = new Newsletter(content);
        if (mapper.update(newsletter, Wrappers.lambdaUpdate(Newsletter.class)
                .eq(Newsletter::getId, id)) >= 1) {
            return ApiResponse.OfStatus(Status.OK);
        }
        return ApiResponse.OfStatus(Status.ERROR);
    }

    @Override
    public ApiResponse removeNewsletter(Integer id) {
        if (mapper.delete(Wrappers.lambdaQuery(Newsletter.class).eq(Newsletter::getId, id)) >= 1) {
            return ApiResponse.OfStatus(Status.OK);
        }
        return ApiResponse.OfStatus(Status.ERROR);
    }
}
