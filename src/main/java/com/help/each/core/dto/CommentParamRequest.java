package com.help.each.core.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author Yuanan
 * @date 2023/4/20
 * @description
 */
@Data
public class CommentParamRequest {
    @NotEmpty(message = "评论的内容不能为空")
    @Length(min = 1, max = 255, message = "评论的内容在1-255个字符之间")
    private String content;
    @NotEmpty(message = "评论者的uuid不能为空")
    //评价人的uuid
    private String uuid;
    @NotNull(message = "评论的服务ID不能为空")
    //评价的服务ID
    private Long serviceId;
}
