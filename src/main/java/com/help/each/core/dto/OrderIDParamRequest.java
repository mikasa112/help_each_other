package com.help.each.core.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderIDParamRequest {
    @NotNull(message = "订单ID不能为空")
    private Long orderId;
}
