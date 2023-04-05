package com.help.each.core.vo;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.help.each.entity.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import static com.help.each.core.constant.Consts.DESC_ORDER;

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

    /**
     * 获取默认规格大小的分页数据
     *
     * @param mapper      {@link BaseMapper<T>}
     * @param wrapper     {@link Wrapper<T>}
     * @param currentPage 当前页
     * @param pageSize    每页多大
     * @param sortBy      以什么排序
     * @param order       排序顺序
     * @param <T>         某些继承于{@link BaseModel} 的类
     * @return 分页数据集合
     */
    public static <T extends BaseModel> List<T> GetDefaultPageList(BaseMapper<T> mapper, Wrapper<T> wrapper, Long currentPage, Long pageSize, String sortBy, String order) {
        Page<T> p = new Page<>();
        p.setSize(pageSize);
        p.setCurrent(currentPage);
        List<OrderItem> orderItemList;
        if (order.equals(DESC_ORDER)) {
            orderItemList = List.of(OrderItem.desc(sortBy));
        } else {
            orderItemList = List.of(OrderItem.asc(sortBy));
        }
        p.setOrders(orderItemList);
        mapper.selectPage(p, wrapper);
        return p.getRecords();
    }
}
