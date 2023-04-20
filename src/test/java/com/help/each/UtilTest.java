package com.help.each;

import com.help.each.core.util.Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Yuanan
 * @date 2023/4/18
 * @description
 */
public class UtilTest {

    @Test
    public void test() {
        Float aFloat = Util.NegativeNum(123F);
        Assertions.assertEquals(aFloat, -123F);
        Long a = 1L;
        Integer b = 3;
        String c = "S";
        String s = Util.ComposeString(a, b, c);
        System.out.println(s);
    }
}
