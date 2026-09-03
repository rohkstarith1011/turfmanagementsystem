package com.crimsonlogic.turfmanagementsystem.util;



import java.util.concurrent.ThreadLocalRandom;

public final class EntityIdGenerator {

    private EntityIdGenerator() {
    }

    public static String generate(String prefix) {
        int number = ThreadLocalRandom.current().nextInt(100000, 1000000);
        return prefix + number;
    }
}