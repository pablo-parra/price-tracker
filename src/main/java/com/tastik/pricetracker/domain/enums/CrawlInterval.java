package com.tastik.pricetracker.domain.enums;

import java.time.Duration;

public enum CrawlInterval {

    EVERY_1H(1),
    EVERY_6H(6),
    EVERY_12H(12),
    EVERY_24H(24);

    private final int hours;

    CrawlInterval(int hours) {
        this.hours = hours;
    }

    public Duration toDuration() {
        return Duration.ofHours(hours);
    }
}
