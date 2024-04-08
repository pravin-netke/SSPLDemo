package com.sspl.demo.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class DashboardTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Dashboard getDashboardSample1() {
        return new Dashboard().id(1L);
    }

    public static Dashboard getDashboardSample2() {
        return new Dashboard().id(2L);
    }

    public static Dashboard getDashboardRandomSampleGenerator() {
        return new Dashboard().id(longCount.incrementAndGet());
    }
}
