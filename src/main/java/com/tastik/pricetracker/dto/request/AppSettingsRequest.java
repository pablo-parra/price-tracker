package com.tastik.pricetracker.dto.request;

import jakarta.validation.constraints.*;

public record AppSettingsRequest(

        @NotBlank(message = "Notification email is required")
        @Email(message = "Must be a valid email address")
        String notificationEmail,

        @Min(value = 0, message = "Daily summary hour must be between 0 and 23")
        @Max(value = 23, message = "Daily summary hour must be between 0 and 23")
        int dailySummaryHour
) {}
