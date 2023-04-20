package com.help.each.core.vo;

import com.help.each.entity.Service;
import com.help.each.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Yuanan
 * @date 2023/4/20
 * @description 返回用户的基本信息和订单信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserInfo {
    private User user;
    private Integer evaluate;
    private List<Service> services;
}
