package com.help.each.core.dto;

import com.help.each.core.constant.Consts;
import lombok.Setter;
import lombok.ToString;


import java.util.Objects;

/**
 * @author Yuanan
 * @date 2023/3/31
 * @description 分页参数请求格式
 */
@Setter
@ToString
public class PageParamRequest {
    private Long page;
    //默认分页大小10页
    private Long size;
    private String sort;
    private String order;

    public Long getPage() {
        //为空|小于0返回1
        if (Objects.isNull(page) || page <= 0L)
            return 1L;
        return page;
    }

    public Long getSize() {
        return size;
    }

    public String getSort() {
        if (Objects.isNull(sort))
            return Consts.SORT_BY;
        return sort;
    }

    public String getOrder() {
        //为空返回或者不是这两个排序顺序返回默认排序
        if (Objects.isNull(order) ||
                (!Objects.equals(order, Consts.ASC_ORDER) && !Objects.equals(order, Consts.DESC_ORDER)))
            return Consts.ASC_ORDER;
        return order;
    }
}
