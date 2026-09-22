package com.alshamel.mall.dto;

import lombok.Data;

/**
 * 批次列表视图（供前端下拉选择，附带产品/仓库简要信息）
 */
@Data
public class BatchView {
    private Long id;
    private String batchNo;
    private Integer quantity;
    private String productSku;
    private String productName;
    private String productUnit;
    private String warehouseCode;
    private String warehouseName;
}
