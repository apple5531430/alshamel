package com.alshamel.mall.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 批量库存调整请求：一次提交多个调整条目
 */
@Data
public class BatchAdjustmentRequest {

    @NotEmpty(message = "items must not be empty")
    @Valid
    private List<Item> items;

    @Data
    public static class Item {
        @NotNull(message = "batchId is required")
        private Long batchId;

        @NotNull(message = "reasonId is required")
        private Long reasonId;

        @NotNull(message = "newQuantity is required")
        @Min(value = 0, message = "newQuantity must be >= 0")
        private Integer newQuantity;

        @Size(max = 500, message = "note must be at most 500 characters")
        private String note;
    }
}
