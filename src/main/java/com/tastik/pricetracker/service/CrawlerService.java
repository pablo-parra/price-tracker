package com.tastik.pricetracker.service;

import com.tastik.pricetracker.domain.entity.PriceSnapshot;
import com.tastik.pricetracker.domain.entity.TrackedProduct;
import com.tastik.pricetracker.exception.CrawlException;
import com.tastik.pricetracker.repository.PriceSnapshotRepository;
import com.tastik.pricetracker.repository.TrackedProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlerService {

    private final TrackedProductRepository productRepository;
    private final PriceSnapshotRepository snapshotRepository;
    private final EmailService emailService;

    @Value("${app.crawler.user-agent}")
    private String userAgent;

    @Value("${app.crawler.timeout-ms}")
    private int timeoutMs;

    @Value("${app.crawler.alert-cooldown-hours}")
    private int alertCooldownHours;

    @Transactional
    public void crawl(TrackedProduct product) {
        log.info("Crawling product '{}' at {}", product.getName(), product.getUrl());
        try {
            BigDecimal price = fetchPrice(product.getUrl(), product.getCssSelector());

            snapshotRepository.save(PriceSnapshot.builder()
                    .product(product)
                    .price(price)
                    .recordedAt(Instant.now())
                    .build());

            product.setCurrentPrice(price);
            product.setLastCrawledAt(Instant.now());
            productRepository.save(product);

            log.info("Price for '{}': {}", product.getName(), price);

            if (isPriceAlertDue(product, price)) {
                emailService.sendPriceAlert(product, price);
                product.setLastAlertSentAt(Instant.now());
                productRepository.save(product);
            }

        } catch (CrawlException e) {
            log.error("Failed to crawl product '{}': {}", product.getName(), e.getMessage());
        }
    }

    private BigDecimal fetchPrice(String url, String cssSelector) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent(userAgent)
                    .timeout(timeoutMs)
                    .get();

            Element element = doc.selectFirst(cssSelector);
            if (element == null) {
                throw new CrawlException(
                        "CSS selector '" + cssSelector + "' matched no element on page: " + url, null);
            }

            return parsePrice(element.text());

        } catch (CrawlException e) {
            throw e;
        } catch (Exception e) {
            throw new CrawlException("Error fetching page: " + url, e);
        }
    }

    private BigDecimal parsePrice(String rawText) {
        String text = rawText.trim();
        // Detect European decimal format: digits, comma, 1-2 digits, no dot (e.g. "19,99")
        String digitsOnly = text.replaceAll("[^\\d.,]", "");
        if (digitsOnly.matches("\\d{1,3}(,\\d{3})+\\.\\d+")) {
            // 1,234.56 — US thousands + decimal dot
            digitsOnly = digitsOnly.replace(",", "");
        } else if (digitsOnly.matches("\\d+,\\d{1,2}")) {
            // 19,99 — European decimal comma
            digitsOnly = digitsOnly.replace(",", ".");
        } else {
            // Remove any remaining commas (thousands separator)
            digitsOnly = digitsOnly.replace(",", "");
        }
        try {
            return new BigDecimal(digitsOnly);
        } catch (NumberFormatException e) {
            throw new CrawlException("Could not parse price from text: '" + rawText + "'", e);
        }
    }

    private boolean isPriceAlertDue(TrackedProduct product, BigDecimal currentPrice) {
        if (currentPrice.compareTo(product.getDesiredPrice()) >= 0) {
            return false;
        }
        if (product.getLastAlertSentAt() == null) {
            return true;
        }
        return product.getLastAlertSentAt()
                .plusSeconds(alertCooldownHours * 3600L)
                .isBefore(Instant.now());
    }
}
