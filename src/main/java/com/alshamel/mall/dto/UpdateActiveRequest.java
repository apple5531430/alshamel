package com.alshamel.mall.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateActiveRequest {

    @NotNull(message = "active is required")
    private Boolean active;
}
