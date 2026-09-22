package com.alshamel.mall.controller;

import com.alshamel.mall.common.ApiResponse;
import com.alshamel.mall.dto.AdjustmentReasonResponse;
import com.alshamel.mall.dto.UpdateActiveRequest;
import com.alshamel.mall.service.AdjustmentReasonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/adjustment-reasons")
@RequiredArgsConstructor
public class AdjustmentReasonController {

    private final AdjustmentReasonService adjustmentReasonService;

    @GetMapping
    public ApiResponse<List<AdjustmentReasonResponse>> listAll() {
        return ApiResponse.success(adjustmentReasonService.listAll());
    }

    @GetMapping("/available")
    public ApiResponse<List<AdjustmentReasonResponse>> listAvailable() {
        return ApiResponse.success(adjustmentReasonService.listAvailable());
    }

    @PutMapping("/{id}/active")
    public ApiResponse<AdjustmentReasonResponse> updateActive(@PathVariable Long id,
                                                              @Valid @RequestBody UpdateActiveRequest request) {
        return ApiResponse.success(adjustmentReasonService.updateActive(id, request));
    }
}
