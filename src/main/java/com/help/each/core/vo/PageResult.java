package com.help.each.core.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author Yuanan
 * @date 2023/3/23
 * @description 通用分页参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 3420391142991247367L;
    private List<T> list;
    private Long total;
    private Long page;
    private Long pageSize;

    /**
     * @param list     集合
     * @param total    总数
     * @param page     当前页数
     * @param pageSize 每页大小
     * @return {@link PageResult}
     */
    public static <T> PageResult<T> Of(List<T> list, Long total, Long page, Long pageSize) {
        return new PageResult<>(list, total, page, pageSize);
    }
}
