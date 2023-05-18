package com.help.each.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.help.each.core.constant.Status;
import com.help.each.core.vo.ApiResponse;
import com.help.each.core.vo.PageResult;
import com.help.each.core.vo.UserInfo;
import com.help.each.entity.Points;
import com.help.each.entity.User;
import com.help.each.mapper.PointsMapper;
import com.help.each.service.PointsService;
import com.help.each.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author Yuanan
 * @date 2023/4/18
 * @description 积分服务实现类
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PointsServiceImpl extends ServiceImpl<PointsMapper, Points> implements PointsService {

    final PointsMapper pointsMapper;
    final UserService userService;

    @Override
    public ApiResponse addPointRecord(String uuid, Long orderId, Float record, String remark) {
        Points points = new Points();
        points.setOrderId(orderId);
        points.setUuid(uuid);
        points.setRemark(remark);
        points.setRecord(record);
        //fix 2023/5/12 cast err
        UserInfo user = (UserInfo) userService.getUserInfoByUuid(uuid).getData();
        //积分改变+-
        user.getUser().setPoints(user.getUser().getPoints() + record);
        if (Objects.equals(userService.updateUserInfo(uuid, user.getUser()).getCode(), Status.OK.getCode())) {
            if (pointsMapper.insert(points) >= 1) {
                return ApiResponse.OfStatus(Status.OK);
            }
        }
        return ApiResponse.OfStatus(Status.POINTS_CREATE_FAILED);
    }

    @Override
    public ApiResponse removePointRecord(Integer id) {
        if (pointsMapper.deleteById(id) >= 1) {
            return ApiResponse.OfStatus(Status.OK);
        }
        return ApiResponse.OfStatus(Status.POINTS_REMOVE_FAILED);
    }

    @Override
    public ApiResponse getPointRecords(Long currentPage, Long pageSize, String sortBy, String order) {
        List<Points> points = PageResult.GetDefaultPageList(pointsMapper, new QueryWrapper<>(), currentPage, pageSize, sortBy, order);
        PageResult<Points> result = PageResult.Of(points, count(), currentPage, pageSize);
        return ApiResponse.OfStatus(Status.OK, result);
    }

    @Override
    public ApiResponse getPointByUUID(String uuid) {
        LambdaQueryWrapper<Points> wrapper = Wrappers.lambdaQuery(Points.class).eq(Points::getUuid, uuid);
        List<Points> points = pointsMapper.selectList(wrapper);
        return ApiResponse.OfStatus(Status.OK, points);
    }
}
