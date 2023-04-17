package com.help.each.core.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author Yuanan
 * @date 2023/4/17
 * @description
 */
@Data
public class NewsletterParamRequest {

    @NotEmpty(message = "简讯的内容不能为空")
    @Length(min = 5, message = "简讯的内容不能小于5个字符")
    String content;
}
