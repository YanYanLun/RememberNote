package com.dreamdesigner.remembernote.utils;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 */
public class IDFactory {
    private static Random random = new Random();
    private static ArrayList<Integer> ids = new ArrayList<>();

    public static int getRandomNumber(final int min, final int max) {
        int tmp = Math.abs(random.nextInt());
        int result = tmp % (max - min + 1) + min;
        return result;
    }

    /**
     * 生成8位数的id
     *
     * @return
     */
    public static int getId() {
        int id = getRandomNumber(10000000, 99999999);
        while (ids.contains(id)) {
            id = getRandomNumber(10000000, 99999999);
        }
        ids.add(id);
        return id;
    }
}
