package com.alshamel.mall.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Warehouse {
    private Long id;
    private String code;
    private String name;
    private String location;
    private LocalDateTime createdAt;
}
