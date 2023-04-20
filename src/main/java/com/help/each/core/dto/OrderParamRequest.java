package com.help.each.core.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author Yuanan
 * @date 2023/4/17
 * @description
 */
@Data
public class OrderParamRequest {

    @NotNull(message = "订单ID不能为空")
    private Long orderId;
    @NotNull(message = "服务人员的评价不能为空")
    @Length(min = 0, max = 5, message = "评价的范围在0-5颗星")
    private Integer evaluate;
}
