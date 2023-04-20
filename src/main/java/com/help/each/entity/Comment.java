package com.help.each.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Yuanan
 * @date 2023/4/20
 * @description 评论
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class Comment extends BaseModel {
    //内容
    private String content;
    //评价人的uuid
    private String uuid;
    //评价的服务ID
    private Long serviceId;
    //点赞数
    private Integer likeCount;
}
