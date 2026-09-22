package com.alshamel.mall.mapper;

import com.alshamel.mall.entity.AdjustmentReason;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdjustmentReasonMapper {

    /** 所有预定义原因（固定集合，仅支持启停） */
    List<AdjustmentReason> findAll();

    /** 启用 + 适用于库存调整（供调整下拉使用） */
    List<AdjustmentReason> findAvailable();

    AdjustmentReason findById(@Param("id") Long id);

    int updateActive(@Param("id") Long id, @Param("active") boolean active);
}
