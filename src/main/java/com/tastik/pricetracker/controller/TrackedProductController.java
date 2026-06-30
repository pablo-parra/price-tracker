package com.tastik.pricetracker.controller;

import com.tastik.pricetracker.dto.request.TrackedProductRequest;
import com.tastik.pricetracker.dto.response.PriceSnapshotResponse;
import com.tastik.pricetracker.dto.response.TrackedProductResponse;
import com.tastik.pricetracker.service.TrackedProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Manage tracked products and view price history")
public class TrackedProductController {

    private final TrackedProductService service;

    @GetMapping
    @Operation(summary = "List all tracked products")
    public List<TrackedProductResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a tracked product by ID")
    public TrackedProductResponse findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a new product to track")
    public TrackedProductResponse create(@RequestBody @Valid TrackedProductRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a tracked product")
    public TrackedProductResponse update(@PathVariable UUID id,
                                         @RequestBody @Valid TrackedProductRequest request) {
        return service.update(id, request);
    }

    @PatchMapping("/{id}/toggle")
    @Operation(summary = "Toggle a product active/inactive")
    public TrackedProductResponse toggle(@PathVariable UUID id) {
        return service.toggleActive(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a tracked product")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @GetMapping("/{id}/history")
    @Operation(summary = "Get price history for a product")
    public List<PriceSnapshotResponse> history(@PathVariable UUID id) {
        return service.getPriceHistory(id);
    }
}
