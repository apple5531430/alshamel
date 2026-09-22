package com.alshamel.mall.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Product {
    private Long id;
    private String sku;
    private String name;
    private String unit;
    private LocalDateTime createdAt;
}
