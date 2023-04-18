package com.help.each.core.util;

/**
 * @author Yuanan
 * @date 2023/4/18
 * @description
 */
public class Util {
    /**
     * 将数变为负数
     *
     * @param target t
     * @return 负数
     */
    public static Float NegativeNum(Float target) {
        target = target - 2 * target;
        return target;
    }
}
