package com.tastik.pricetracker.dto.request;

import com.tastik.pricetracker.domain.enums.CrawlInterval;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

public record TrackedProductRequest(

        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "URL is required")
        @URL(message = "Must be a valid URL")
        String url,

        @NotBlank(message = "CSS selector is required")
        String cssSelector,

        @NotNull(message = "Desired price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Desired price must be greater than 0")
        BigDecimal desiredPrice,

        @NotNull(message = "Crawl interval is required")
        CrawlInterval crawlInterval
) {}
