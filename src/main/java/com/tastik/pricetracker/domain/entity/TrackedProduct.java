package com.tastik.pricetracker.domain.entity;

import com.tastik.pricetracker.domain.enums.CrawlInterval;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tracked_products")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackedProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 2048)
    private String url;

    @Column(name = "css_selector", nullable = false)
    private String cssSelector;

    @Column(name = "current_price", precision = 10, scale = 2)
    private BigDecimal currentPrice;

    @Column(name = "desired_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal desiredPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "crawl_interval", nullable = false)
    private CrawlInterval crawlInterval;

    @Column(name = "last_crawled_at")
    private Instant lastCrawledAt;

    @Column(name = "last_alert_sent_at")
    private Instant lastAlertSentAt;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void prePersist() {
        createdAt = Instant.now();
    }
}
