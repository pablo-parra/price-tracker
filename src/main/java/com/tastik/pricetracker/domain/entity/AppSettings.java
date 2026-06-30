package com.tastik.pricetracker.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "app_settings")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppSettings {

    @Id
    private Long id;

    @Column(name = "notification_email", nullable = false)
    private String notificationEmail;

    @Column(name = "daily_summary_hour", nullable = false)
    private int dailySummaryHour;
}
