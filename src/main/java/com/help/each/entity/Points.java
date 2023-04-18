package com.help.each.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Yuanan
 * @date 2023/4/18
 * @description 积分的记录
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class Points extends BaseModel {
    /*
    用户UUID
     */
    String uuid;
    /*
    订单id
     */
    Long orderId;
    /*
    积分的记录
     */
    Float record;
    /*
    备注
     */
    String remark;
}
