package com.alshamel.mall.service;

import com.alshamel.mall.dto.AdjustmentReasonResponse;
import com.alshamel.mall.dto.UpdateActiveRequest;
import com.alshamel.mall.entity.AdjustmentReason;
import com.alshamel.mall.exception.BusinessException;
import com.alshamel.mall.mapper.AdjustmentReasonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdjustmentReasonService {

    private final AdjustmentReasonMapper reasonMapper;

    /** 所有预定义原因（管理页） */
    public List<AdjustmentReasonResponse> listAll() {
        return reasonMapper.findAll().stream().map(this::toResponse).toList();
    }

    /** 启用 + 适用于库存调整的原因（调整下拉） */
    public List<AdjustmentReasonResponse> listAvailable() {
        return reasonMapper.findAvailable().stream().map(this::toResponse).toList();
    }

    public AdjustmentReasonResponse updateActive(Long id, UpdateActiveRequest request) {
        AdjustmentReason reason = reasonMapper.findById(id);
        if (reason == null) {
            throw BusinessException.notFound("Reason not found: " + id);
        }
        reasonMapper.updateActive(id, request.getActive());
        reason.setActive(request.getActive());
        return toResponse(reason);
    }

    private AdjustmentReasonResponse toResponse(AdjustmentReason r) {
        return new AdjustmentReasonResponse(r.getId(), r.getCode(), r.getName(), r.getType(), r.isActive());
    }
}
