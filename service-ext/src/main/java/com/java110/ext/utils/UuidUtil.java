package com.java110.ext.utils;

import java.util.UUID;

public class UuidUtil {

    /**
     * 生成 32 位 UUID（去掉横杠 -）
     */
    public static String get32UUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
