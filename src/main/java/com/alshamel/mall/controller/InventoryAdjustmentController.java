package com.alshamel.mall.controller;

import com.alshamel.mall.common.ApiResponse;
import com.alshamel.mall.dto.BatchAdjustmentRequest;
import com.alshamel.mall.dto.CreateAdjustmentRequest;
import com.alshamel.mall.dto.InventoryAdjustmentView;
import com.alshamel.mall.service.InventoryAdjustmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/inventory-adjustments")
@RequiredArgsConstructor
public class InventoryAdjustmentController {

    private final InventoryAdjustmentService inventoryAdjustmentService;

    @GetMapping
    public ApiResponse<List<InventoryAdjustmentView>> list() {
        return ApiResponse.success(inventoryAdjustmentService.listAll());
    }

    @PostMapping
    public ApiResponse<InventoryAdjustmentView> create(@Valid @RequestBody CreateAdjustmentRequest request,
                                                       Authentication authentication) {
        return ApiResponse.success(inventoryAdjustmentService.create(request, authentication.getName()));
    }

    @PostMapping("/batch")
    public ApiResponse<List<InventoryAdjustmentView>> createBatch(@Valid @RequestBody BatchAdjustmentRequest request,
                                                                  Authentication authentication) {
        return ApiResponse.success(inventoryAdjustmentService.createBatch(request, authentication.getName()));
    }

    @GetMapping("/{id}")
    public ApiResponse<InventoryAdjustmentView> get(@PathVariable Long id) {
        return ApiResponse.success(inventoryAdjustmentService.getById(id));
    }
}
