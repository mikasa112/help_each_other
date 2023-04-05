package com.help.each.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Yuanan
 * @date 2023/4/5
 * @description 服务
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Service extends BaseModel {
    /*
    服务ID
     */
    private Long serviceId;
    /*
    服务创建者的uuid
     */
    private String uuid;
    /*
    服务的名称
     */
    private String name;
    /*
    服务的介绍
     */
    private String introduction;
    /*
    服务的关键字
     */
    private String keywords;
    /*
    服务的价格(积分)
     */
    private Double pointsPrice;
    /*
    服务的相关图片(以;分割)
     */
    private String pictures;
    /*
    所需服务的地址
     */
    private String address;
}
