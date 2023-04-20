package com.help.each.core.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author Yuanan
 * @date 2023/4/5
 * @description 添加服务DTO
 */
@Data
public class AddServiceRequest {
    /*
    服务的名称
     */
    @NotEmpty(message = "服务的名称不能为空")
    @Length(min = 3, max = 255, message = "服务的名称长度在3-255个字符之间")
    private String name;
    /*
    服务的介绍
     */
    @Length(max = 255, message = "服务的详细介绍最长为255个字符")
    private String introduction;
    /*
    服务的关键字
     */
    @Length(max = 255, message = "服务的关键字最长为255个字符")
    private String keywords;

    @Min(value = 0L, message = "服务的价格最小不能为0")
    private Float pointsPrice;

    private String pictures;
    /*
    所需服务的地址
     */
    @Length(max = 255, message = "所需服务的地址最长为255个字符")
    private String address;

    @NotNull(message = "服务分类的ID不能为空")
    private Integer category;

}
