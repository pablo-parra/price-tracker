package com.tastik.pricetracker.service;

import com.tastik.pricetracker.domain.entity.TrackedProduct;
import com.tastik.pricetracker.repository.TrackedProductRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final AppSettingsService settingsService;
    private final TrackedProductRepository productRepository;

    public void sendPriceAlert(TrackedProduct product, BigDecimal currentPrice) {
        String to = settingsService.getSettings().getNotificationEmail();
        String subject = String.format("Price Alert: %s is now %s", product.getName(), currentPrice);

        Context context = new Context();
        context.setVariable("product", product);
        context.setVariable("currentPrice", currentPrice);

        sendHtmlEmail(to, subject, "email/price-alert", context);
        log.info("Price alert sent to {} for product '{}'", to, product.getName());
    }

    public void sendDailySummary() {
        String to = settingsService.getSettings().getNotificationEmail();
        List<TrackedProduct> products = productRepository.findByActiveTrue();

        if (products.isEmpty()) {
            log.info("No active products — skipping daily summary email");
            return;
        }

        Context context = new Context();
        context.setVariable("products", products);

        sendHtmlEmail(to, "PriceTracker — Daily Summary", "email/daily-summary", context);
        log.info("Daily summary sent to {} for {} products", to, products.size());
    }

    private void sendHtmlEmail(String to, String subject, String template, Context context) {
        try {
            String html = templateEngine.process(template, context);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("Failed to send email '{}' to {}: {}", subject, to, e.getMessage());
        }
    }
}
