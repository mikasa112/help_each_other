package com.help.each.controller.v1;

import com.help.each.core.dto.CommentParamRequest;
import com.help.each.core.dto.CommentUpdateParamRequest;
import com.help.each.core.dto.PageParamRequest;
import com.help.each.core.vo.ApiResponse;
import com.help.each.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Yuanan
 * @date 2023/4/20
 * @description 评论相关接口
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("api/v1/comment")
public class CommentController {
    final CommentService service;


    @GetMapping("{serviceId}")
    public ApiResponse index(@PathVariable("serviceId") Long serviceId, PageParamRequest request) {
        return service.getCommentsByServiceId(serviceId, request.getPage(), request.getSize(), request.getSort(), request.getOrder());
    }

    @PostMapping
    public ApiResponse add(@RequestBody @Valid CommentParamRequest request) {
        return service.addComment(request.getContent(), request.getUuid(), request.getServiceId());
    }

    @PutMapping("{id}")
    public ApiResponse update(@PathVariable("id") Integer id, @RequestBody @Valid CommentUpdateParamRequest request) {
        return service.updateComment(id, request.getContent());
    }

    @PutMapping("like/{id}")
    public ApiResponse likeThis(@PathVariable("id") Integer id) {
        return service.likeThis(id);
    }

    @DeleteMapping("{id}")
    public ApiResponse remove(@PathVariable("id") Integer id) {
        return service.removeComment(id);
    }
}
