package com.help.each.core.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author Yuanan
 * @date 2023/4/24
 * @description 添加积分
 */
@Data
public class AddPointsParamRequest {
    @NotEmpty(message = "用户的UUID不能为空")
    private String uuid;
    @Min(value = 0L, message = "积分不能小于0")
    private Float record;
}
