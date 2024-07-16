package com.zxb.auto_build;

/**
 * @author Mr.M
 * @date 2024/7/12
 * @Description
 */
public class CommonUtil {
    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean notNull(Object obj) {
        return !isNull(obj);
    }
}
