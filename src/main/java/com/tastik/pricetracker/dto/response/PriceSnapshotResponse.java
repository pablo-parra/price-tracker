package com.tastik.pricetracker.dto.response;

import com.tastik.pricetracker.domain.entity.PriceSnapshot;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PriceSnapshotResponse(
        UUID id,
        BigDecimal price,
        Instant recordedAt
) {
    public static PriceSnapshotResponse fromEntity(PriceSnapshot s) {
        return new PriceSnapshotResponse(s.getId(), s.getPrice(), s.getRecordedAt());
    }
}
