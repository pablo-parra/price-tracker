package com.tastik.pricetracker.scheduler;

import com.tastik.pricetracker.domain.entity.TrackedProduct;
import com.tastik.pricetracker.repository.TrackedProductRepository;
import com.tastik.pricetracker.service.CrawlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CrawlerScheduler {

    private final TrackedProductRepository productRepository;
    private final CrawlerService crawlerService;

    @Scheduled(cron = "0 0 * * * *")
    public void run() {
        List<TrackedProduct> products = productRepository.findByActiveTrue();
        if (products.isEmpty()) {
            return;
        }
        log.info("Crawler tick — checking {} active product(s)", products.size());
        Instant now = Instant.now();
        products.stream()
                .filter(p -> isDue(p, now))
                .forEach(crawlerService::crawl);
    }

    private boolean isDue(TrackedProduct product, Instant now) {
        if (product.getLastCrawledAt() == null) {
            return true;
        }
        return product.getLastCrawledAt()
                .plus(product.getCrawlInterval().toDuration())
                .isBefore(now);
    }
}
