package com.help.each.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author Yuanan
 * @date 2023/4/14
 * @description 服务订单model
 */
@TableName(value = "help_service_order")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Data
public class Order extends BaseModel {
    /*
    订单雪花ID
     */
    private Long orderId;
    /*
       服务ID
        */
    private Long serviceId;
    /*
        接单人的uuid
     */
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String providerUuid;
    /*
        顾客的UUID
     */
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String customerUuid;
    /*
    订单状态(0等待顾客同意，1正在进行，2已完成，3异常，4取消)
     */
    private Integer status;
    /*
    订单开始时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private LocalDateTime startAt;
    /*
       订单结束时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private LocalDateTime endAt;
    /*
    订单支付状态(0未支付,1已支付)
     */
    private Integer pay;
}
