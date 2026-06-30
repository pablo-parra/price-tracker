package com.tastik.pricetracker.dto.response;

import com.tastik.pricetracker.domain.entity.AppSettings;

public record AppSettingsResponse(
        String notificationEmail,
        int dailySummaryHour
) {
    public static AppSettingsResponse fromEntity(AppSettings s) {
        return new AppSettingsResponse(s.getNotificationEmail(), s.getDailySummaryHour());
    }
}
