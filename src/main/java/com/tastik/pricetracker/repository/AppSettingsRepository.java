package com.tastik.pricetracker.repository;

import com.tastik.pricetracker.domain.entity.AppSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppSettingsRepository extends JpaRepository<AppSettings, Long> {
}
