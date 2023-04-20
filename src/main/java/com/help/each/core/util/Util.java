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

    /**
     * 将基本类型转化为String拼接
     *
     * @param obj Object
     * @return like "xxx-aaa-bbb"
     */
    public static String ComposeString(Object... obj) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < obj.length; i++) {
            sb.append(obj[i]);
            if (i != obj.length - 1) {
                sb.append('-');
            }
        }
        return sb.toString();
    }
}
