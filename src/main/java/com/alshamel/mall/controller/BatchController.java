package com.alshamel.mall.controller;

import com.alshamel.mall.common.ApiResponse;
import com.alshamel.mall.dto.BatchView;
import com.alshamel.mall.service.BatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/batches")
@RequiredArgsConstructor
public class BatchController {

    private final BatchService batchService;

    @GetMapping
    public ApiResponse<List<BatchView>> list() {
        return ApiResponse.success(batchService.listAll());
    }
}
