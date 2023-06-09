package com.help.each.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Objects;

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
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String uuid;
    /*
    服务的名称
     */
    private String name;
    /*
    服务的介绍
     */
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String introduction;
    /*
    服务的关键字
     */
    private String keywords;
    /*
    服务的价格(积分)
     */
    private Float pointsPrice;
    /*
    服务的相关图片(以;分割)
     */
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String pictures;
    /*
    所需服务的地址
     */
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String address;
    /*
      服务浏览的次数
      2023/5/17 fix to Double
     */
    @TableField(exist = false)
    private Double visited;

    /*
    服务的状态 0:未接单，1:已接单
     */
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Integer status;
    /*
       2023/5/14 添加; 服务地址的经度
     */
    private Double longitude;
    /*
       2023/5/14 添加; 服务地址的纬度
     */
    private Double latitude;

    /*
    服务的分类
    */
    private Integer category;

    public Double getVisited() {
        if (Objects.isNull(visited)) {
            return 0D;
        }
        return visited;
    }
}
