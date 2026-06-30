package com.tastik.pricetracker.service;

import com.tastik.pricetracker.domain.entity.TrackedProduct;
import com.tastik.pricetracker.dto.request.TrackedProductRequest;
import com.tastik.pricetracker.dto.response.PriceSnapshotResponse;
import com.tastik.pricetracker.dto.response.TrackedProductResponse;
import com.tastik.pricetracker.exception.ResourceNotFoundException;
import com.tastik.pricetracker.repository.PriceSnapshotRepository;
import com.tastik.pricetracker.repository.TrackedProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrackedProductService {

    private final TrackedProductRepository productRepository;
    private final PriceSnapshotRepository snapshotRepository;

    public List<TrackedProductResponse> findAll() {
        return productRepository.findAll().stream()
                .map(TrackedProductResponse::fromEntity)
                .toList();
    }

    public TrackedProductResponse findById(UUID id) {
        return TrackedProductResponse.fromEntity(getOrThrow(id));
    }

    @Transactional
    public TrackedProductResponse create(TrackedProductRequest request) {
        TrackedProduct product = TrackedProduct.builder()
                .name(request.name())
                .url(request.url())
                .cssSelector(request.cssSelector())
                .desiredPrice(request.desiredPrice())
                .crawlInterval(request.crawlInterval())
                .active(true)
                .build();
        return TrackedProductResponse.fromEntity(productRepository.save(product));
    }

    @Transactional
    public TrackedProductResponse update(UUID id, TrackedProductRequest request) {
        TrackedProduct product = getOrThrow(id);
        product.setName(request.name());
        product.setUrl(request.url());
        product.setCssSelector(request.cssSelector());
        product.setDesiredPrice(request.desiredPrice());
        product.setCrawlInterval(request.crawlInterval());
        return TrackedProductResponse.fromEntity(productRepository.save(product));
    }

    @Transactional
    public TrackedProductResponse toggleActive(UUID id) {
        TrackedProduct product = getOrThrow(id);
        product.setActive(!product.isActive());
        return TrackedProductResponse.fromEntity(productRepository.save(product));
    }

    @Transactional
    public void delete(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    public List<PriceSnapshotResponse> getPriceHistory(UUID id) {
        TrackedProduct product = getOrThrow(id);
        return snapshotRepository.findByProductOrderByRecordedAtDesc(product).stream()
                .map(PriceSnapshotResponse::fromEntity)
                .toList();
    }

    private TrackedProduct getOrThrow(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }
}
