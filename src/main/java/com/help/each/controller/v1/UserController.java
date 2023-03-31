package com.help.each.controller.v1;

import com.google.common.base.CharMatcher;
import com.help.each.config.AppConfig;
import com.help.each.core.constant.Status;
import com.help.each.core.dto.PageParamRequest;
import com.help.each.core.dto.RegisterRequest;
import com.help.each.core.vo.ApiResponse;
import com.help.each.core.vo.PageResult;
import com.help.each.entity.User;
import com.help.each.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * @author Yuanan
 * @date 2023/3/31
 * @description 用户管理接口
 */
@Validated
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user")
public class UserController {

    final UserService service;

    final AppConfig appConfig;

    @GetMapping
    @Secured("admin")
    public ApiResponse index(PageParamRequest request) {
        if (Objects.isNull(request.getSize())) {
            request.setSize(appConfig.getPageSize());
        }
        PageResult<User> list = service.list(request.getPage(), request.getSize(), request.getSort(), request.getOrder());
        return ApiResponse.OfStatus(Status.OK, list);
    }

    @GetMapping("{uuid}")
    public ApiResponse index(@PathVariable("uuid") String uuid) {
        User user = service.getUserInfoByUuid(uuid);
        return ApiResponse.OfStatus(Status.OK, user);
    }

    @PutMapping("{uuid}")
    public ApiResponse index(@PathVariable("uuid") String uuid, @Valid RegisterRequest request) {
        User user = new User();//todo 没有参数记得修改
        boolean b = service.updateUserInfo(uuid, user);
        return ApiResponse.PrintlnApiResponse(b, "更新成功", Status.USER_UPDATE_FAILED);
    }
}
