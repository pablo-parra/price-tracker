package com.tastik.pricetracker.config;

import com.tastik.pricetracker.domain.entity.AppSettings;
import com.tastik.pricetracker.repository.AppSettingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import static com.tastik.pricetracker.service.AppSettingsService.SETTINGS_ID;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final AppSettingsRepository repository;

    @Override
    public void run(ApplicationArguments args) {
        if (!repository.existsById(SETTINGS_ID)) {
            repository.save(AppSettings.builder()
                    .id(SETTINGS_ID)
                    .notificationEmail("user@example.com")
                    .dailySummaryHour(8)
                    .build());
            log.info("Default app settings created — update via PUT /api/settings");
        }
    }
}
