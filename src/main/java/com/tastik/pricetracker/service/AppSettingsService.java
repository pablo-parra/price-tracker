package com.tastik.pricetracker.service;

import com.tastik.pricetracker.domain.entity.AppSettings;
import com.tastik.pricetracker.dto.request.AppSettingsRequest;
import com.tastik.pricetracker.dto.response.AppSettingsResponse;
import com.tastik.pricetracker.repository.AppSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AppSettingsService {

    public static final long SETTINGS_ID = 1L;

    private final AppSettingsRepository repository;

    public AppSettings getSettings() {
        return repository.findById(SETTINGS_ID)
                .orElseThrow(() -> new IllegalStateException("App settings not initialised"));
    }

    public AppSettingsResponse getSettingsResponse() {
        return AppSettingsResponse.fromEntity(getSettings());
    }

    @Transactional
    public AppSettingsResponse update(AppSettingsRequest request) {
        AppSettings settings = getSettings();
        settings.setNotificationEmail(request.notificationEmail());
        settings.setDailySummaryHour(request.dailySummaryHour());
        return AppSettingsResponse.fromEntity(repository.save(settings));
    }
}
