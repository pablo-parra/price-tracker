package com.tastik.pricetracker.dto.response;

import com.tastik.pricetracker.domain.entity.TrackedProduct;
import com.tastik.pricetracker.domain.enums.CrawlInterval;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TrackedProductResponse(
        UUID id,
        String name,
        String url,
        String cssSelector,
        BigDecimal currentPrice,
        BigDecimal desiredPrice,
        CrawlInterval crawlInterval,
        Instant lastCrawledAt,
        boolean active,
        Instant createdAt
) {
    public static TrackedProductResponse fromEntity(TrackedProduct p) {
        return new TrackedProductResponse(
                p.getId(), p.getName(), p.getUrl(), p.getCssSelector(),
                p.getCurrentPrice(), p.getDesiredPrice(), p.getCrawlInterval(),
                p.getLastCrawledAt(), p.isActive(), p.getCreatedAt()
        );
    }
}
