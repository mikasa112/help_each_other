package com.help.each.controller.v1;

import com.help.each.config.AppConfig;
import com.help.each.core.dto.PageParamRequest;
import com.help.each.core.dto.UpdateUserInfoRequest;
import com.help.each.core.vo.ApiResponse;
import com.help.each.entity.MyUserDetails;
import com.help.each.entity.User;
import com.help.each.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    /*
    用户全部信息接口
     */
    @GetMapping
    @Secured("admin")
    public ApiResponse index(PageParamRequest request) {
        return service.list(request.getPage(), request.getSize(), request.getSort(), request.getOrder());
    }

    /*
    uuid的用户的信息
     */
    @GetMapping("{uuid}")
    public ApiResponse index(@PathVariable("uuid") String uuid) {
        return service.getUserInfoByUuid(uuid);
    }

    /*
    获取用户以用户名字
     */
    @GetMapping("name/{name}")
    public ApiResponse name(@PathVariable("name") String name) {
        return service.getUserInfoByName(name);
    }

    /*
    获取当前登录用户的信息
     */
    @GetMapping("index")
    public ApiResponse index(Authentication authentication) {
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        String uuid = userDetails.getUser().getUuid();
        return service.getUserInfoByUuid(uuid);
    }

    /*
    修改uuid的用户的信息
     */
    @PutMapping("{uuid}")
    public ApiResponse index(@PathVariable("uuid") String uuid, @RequestBody @Valid UpdateUserInfoRequest request) {
        User user = new User();
        user.setPassword(request.getPassword());
        user.setNickname(request.getNickname());
        user.setAvatar(request.getAvatar());
        user.setAge(request.getAge());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setSex(request.getSex());
        return service.updateUserInfo(uuid, user);
    }

    /*
    删除uuid的用户
     */
    @DeleteMapping("{uuid}")
    @Secured("admin")
    public ApiResponse remove(@PathVariable("uuid") String uuid) {
        return service.removeUserByUuid(uuid);
    }
}
