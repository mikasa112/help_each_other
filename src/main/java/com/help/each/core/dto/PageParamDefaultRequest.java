package com.help.each.core.dto;

import com.help.each.core.constant.Consts;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

/**
 * @author Yuanan
 * @date 2023/4/8
 * @description
 */
@Setter
@ToString
public class PageParamDefaultRequest {
    private Long page;
    //默认分页大小10页
    private Long size;

    public Long getPage() {
        //为空或者小于0返回1
        if (Objects.isNull(page) || page <= 0L)
            return 1L;
        return page;
    }

    public Long getSize() {
        if (Objects.isNull(size) || size <= 0L)
            return Consts.DEFAULT_PAGE_SIZE;
        return size;
    }
}
