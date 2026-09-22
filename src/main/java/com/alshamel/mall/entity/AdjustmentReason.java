package com.alshamel.mall.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdjustmentReason {
    private Long id;
    private String code;
    private String name;
    /** 适用调整类型：INVENTORY / OTHER */
    private String type;
    /** 是否启用 */
    private boolean active;
    private LocalDateTime createdAt;
}
