package com.alshamel.mall.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 库存调整详情视图：调整记录 + 批次 + 产品 + 仓库 + 原因 + 操作人
 */
@Data
public class InventoryAdjustmentView {
    private Long id;
    private Integer oldQuantity;
    private Integer newQuantity;
    private Integer quantityDiff;
    private String note;
    private LocalDateTime createdAt;
    private String adjustedBy;
    private BatchInfo batch;
    private ReasonInfo reason;

    @Data
    public static class BatchInfo {
        private Long id;
        private String batchNo;
        private Integer quantity;
        private ProductInfo product;
        private WarehouseInfo warehouse;
    }

    @Data
    public static class ProductInfo {
        private Long id;
        private String sku;
        private String name;
        private String unit;
    }

    @Data
    public static class WarehouseInfo {
        private Long id;
        private String code;
        private String name;
        private String location;
    }

    @Data
    public static class ReasonInfo {
        private Long id;
        private String code;
        private String name;
    }
}
