package com.help.each.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Yuanan
 * @date 2023/3/23
 * @description 基本字段模型
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseModel implements Serializable {
    /**
     * 自增ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 创建时间自动填充
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    /**
     * 更新或者插入时间自动填充
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    /**
     * 删除时间于
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @TableField(value = "deleted_at")
    @TableLogic(value = "null", delval = "now()")
    private LocalDateTime deletedAt;

}