package com.help.each.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Yuanan
 * @date 2023/4/19
 * @description 服务分类
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ServiceCategory extends BaseModel {
    //服务分类名称
    private String category;
    //服务分类描述
    private String describes;
    //备注
    private String notes;

}
