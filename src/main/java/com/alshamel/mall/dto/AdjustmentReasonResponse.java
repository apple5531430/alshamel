package com.alshamel.mall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdjustmentReasonResponse {
    private Long id;
    private String code;
    private String name;
    private String type;
    private boolean active;
}
