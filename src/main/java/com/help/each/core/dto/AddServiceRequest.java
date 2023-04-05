package com.help.each.core.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
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
    服务创建者的uuid
     */
    @NotEmpty(message = "创建者的uuid不能为空")
    private String uuid;
    /*
    服务的名称
     */
    @NotEmpty(message = "服务的名称不能为空")
    @Length(min = 3, max = 255, message = "服务的名称长度在3-255个字符之间")
    private String name;
    /*
    服务的介绍
     */
    @Max(value = 255, message = "服务的详细介绍最长为255个字符")
    private String introduction;
    /*
    服务的关键字
     */
    @Max(value = 255, message = "服务的关键字最长为255个字符")
    private String keywords;
    /*
    所需服务的地址
     */
    @Max(value = 255, message = "所需服务的地址最长为255个字符")
    private String address;
}
