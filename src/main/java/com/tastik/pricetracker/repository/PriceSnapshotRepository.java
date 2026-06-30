package com.tastik.pricetracker.repository;

import com.tastik.pricetracker.domain.entity.PriceSnapshot;
import com.tastik.pricetracker.domain.entity.TrackedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PriceSnapshotRepository extends JpaRepository<PriceSnapshot, UUID> {

    List<PriceSnapshot> findByProductOrderByRecordedAtDesc(TrackedProduct product);
}
