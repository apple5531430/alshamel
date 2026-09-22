package com.alshamel.mall.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Batch {
    private Long id;
    private String batchNo;
    private Long productId;
    private Long warehouseId;
    private Integer quantity;
    private LocalDateTime createdAt;
}
