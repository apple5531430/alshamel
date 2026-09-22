package com.alshamel.mall.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InventoryAdjustment {
    private Long id;
    private Long batchId;
    private Long reasonId;
    private Long userId;
    private Integer oldQuantity;
    private Integer newQuantity;
    private Integer quantityDiff;
    private String note;
    private LocalDateTime createdAt;
}
