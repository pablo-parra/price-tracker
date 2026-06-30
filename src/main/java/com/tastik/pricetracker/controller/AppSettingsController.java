package com.tastik.pricetracker.controller;

import com.tastik.pricetracker.dto.request.AppSettingsRequest;
import com.tastik.pricetracker.dto.response.AppSettingsResponse;
import com.tastik.pricetracker.service.AppSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
@Tag(name = "Settings", description = "Manage notification email and daily summary schedule")
public class AppSettingsController {

    private final AppSettingsService service;

    @GetMapping
    @Operation(summary = "Get current application settings")
    public AppSettingsResponse get() {
        return service.getSettingsResponse();
    }

    @PutMapping
    @Operation(summary = "Update application settings")
    public AppSettingsResponse update(@RequestBody @Valid AppSettingsRequest request) {
        return service.update(request);
    }
}
