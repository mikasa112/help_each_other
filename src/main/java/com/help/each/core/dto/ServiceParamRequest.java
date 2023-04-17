package com.help.each.core.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Yuanan
 * @date 2023/4/17
 * @description
 */
@Data
public class ServiceParamRequest {
    @NotNull(message = "服务ID不能为空")
    private Long serviceId;
}
