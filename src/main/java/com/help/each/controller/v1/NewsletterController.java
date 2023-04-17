package com.help.each.controller.v1;

import com.help.each.core.dto.NewsletterParamRequest;
import com.help.each.core.dto.PageParamRequest;
import com.help.each.core.vo.ApiResponse;
import com.help.each.service.NewsletterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Yuanan
 * @date 2023/4/17
 * @description
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("api/v1/newsletter")
public class NewsletterController {
    final NewsletterService service;

    @GetMapping
    public ApiResponse index(PageParamRequest paramRequest) {
        return service.getNewsletter(paramRequest.getPage(), paramRequest.getSize(), paramRequest.getSort(), paramRequest.getOrder());
    }

    @PostMapping
    public ApiResponse put(@RequestBody @Valid NewsletterParamRequest request) {
        return service.putNewsletter(request.getContent());
    }

    @PutMapping("{id}")
    public ApiResponse update(@PathVariable("id") Integer id, @RequestBody @Valid NewsletterParamRequest request) {
        return service.updateNewsletter(id, request.getContent());
    }

    @DeleteMapping("{id}")
    public ApiResponse remove(@PathVariable("id") Integer id) {
        return service.removeNewsletter(id);
    }
}
