package com.tastik.pricetracker.scheduler;

import com.tastik.pricetracker.service.AppSettingsService;
import com.tastik.pricetracker.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.ZoneId;

@Slf4j
@Component
@RequiredArgsConstructor
public class DailySummaryScheduler {

    private final AppSettingsService settingsService;
    private final EmailService emailService;

    @Scheduled(cron = "0 0 * * * *")
    public void run() {
        int configuredHour = settingsService.getSettings().getDailySummaryHour();
        int currentHour = LocalTime.now(ZoneId.systemDefault()).getHour();
        if (currentHour == configuredHour) {
            log.info("Sending daily summary email");
            emailService.sendDailySummary();
        }
    }
}
