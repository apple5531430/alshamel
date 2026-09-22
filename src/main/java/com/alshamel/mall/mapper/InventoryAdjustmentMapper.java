package com.alshamel.mall.mapper;

import com.alshamel.mall.dto.InventoryAdjustmentView;
import com.alshamel.mall.entity.InventoryAdjustment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InventoryAdjustmentMapper {

    int insert(InventoryAdjustment adjustment);

    /** 联表查询单条调整记录及其批次/产品/仓库/原因/操作人 */
    InventoryAdjustmentView selectWithDetailsById(@Param("id") Long id);

    /** 联表查询所有调整记录（最新在前） */
    List<InventoryAdjustmentView> selectAllWithDetails();
}
