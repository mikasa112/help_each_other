package com.help.each.core.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author Yuanan
 * @date 2023/4/19
 * @description
 */
@Data
public class CategoryParamRequest {
    //服务分类名称
    @NotEmpty(message = "分类的名称不能为空")
    @Length(min = 3, message = "名称长度最小为3个字符")
    private String category;
    //服务分类描述
    private String describe;
    //备注
    private String notes;
}
