package com.sios.stc.coseng.util;

import java.util.concurrent.ThreadLocalRandom;

public class Random {

    public static int getRandomInt(int origin, int bound) throws IllegalArgumentException {
        /* Exception if origin is >= bound */
        return ThreadLocalRandom.current().nextInt(origin, bound);
    }

    public static boolean getRandomBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }

}
