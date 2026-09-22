package com.alshamel.mall.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateAdjustmentRequest {

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
