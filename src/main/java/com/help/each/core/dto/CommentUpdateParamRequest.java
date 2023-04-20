package com.help.each.core.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author Yuanan
 * @date 2023/4/20
 * @description
 */
@Data
public class CommentUpdateParamRequest {
    @NotEmpty(message = "评论的内容不能为空")
    @Length(min = 1, max = 255, message = "评论的内容在1-255个字符之间")
    private String content;
}
