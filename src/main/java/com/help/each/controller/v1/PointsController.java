package com.help.each.controller.v1;

import com.help.each.core.constant.Consts;
import com.help.each.core.dto.AddPointsParamRequest;
import com.help.each.core.dto.PageParamRequest;
import com.help.each.core.vo.ApiResponse;
import com.help.each.entity.MyUserDetails;
import com.help.each.service.PointsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Yuanan
 * @date 2023/4/18
 * @description 积分相关接口
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("api/v1/points")
public class PointsController {
    final PointsService pointsService;

    @Secured("admin")
    @GetMapping
    public ApiResponse index(PageParamRequest request) {
        return pointsService.getPointRecords(request.getPage(),
                request.getSize(), request.getSort(), request.getOrder());
    }

    /*
        GET  api/v1/points/mine
     */
    @GetMapping("mine")
    public ApiResponse index(Authentication authentication) {
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        String uuid = userDetails.getUser().getUuid();
        return pointsService.getPointByUUID(uuid);
    }

    /*
        管理员添加积分
     */
    @Secured("admin")
    @PostMapping
    public ApiResponse addPoint(@RequestBody @Validated AddPointsParamRequest request) {
        return pointsService.addPointRecord(request.getUuid(), null, request.getRecord(), Consts.SYS_POINT_REMARK_ADD);
    }

    @Secured("admin")
    @DeleteMapping("{id}")
    public ApiResponse remove(@PathVariable("id") Integer id) {
        return pointsService.removePointRecord(id);
    }
}
