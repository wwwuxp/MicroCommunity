package com.java110.ext.utils;

import com.java110.utils.util.DateUtil;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class KeyUtils {
    private static final long ONE_STEP = 1000000;
    private static final Lock LOCK = new ReentrantLock();
    private static short lastCount = 1;
    private static int count = 0;
    public static String genKey(String prefix){
        return prefix + DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_H) + nextId("%04d");

    }
    public static String nextId(String idLength) {
        LOCK.lock();
        try {
            if (lastCount == ONE_STEP) {
                lastCount = 1;
            }
            count = lastCount++;
        } finally {
            LOCK.unlock();
            return getRandom() + String.format(idLength, count);
        }
    }

    private static String getRandom() {
        Random random = new Random();
        String result = "";
        for (int i = 0; i < 4; i++) {
            result += random.nextInt(10);
        }
        return result;
    }
}
