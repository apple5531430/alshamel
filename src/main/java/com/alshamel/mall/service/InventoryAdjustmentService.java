package com.alshamel.mall.service;

import com.alshamel.mall.dto.BatchAdjustmentRequest;
import com.alshamel.mall.dto.CreateAdjustmentRequest;
import com.alshamel.mall.dto.InventoryAdjustmentView;
import com.alshamel.mall.entity.AdjustmentReason;
import com.alshamel.mall.entity.Batch;
import com.alshamel.mall.entity.InventoryAdjustment;
import com.alshamel.mall.entity.User;
import com.alshamel.mall.exception.BusinessException;
import com.alshamel.mall.mapper.AdjustmentReasonMapper;
import com.alshamel.mall.mapper.BatchMapper;
import com.alshamel.mall.mapper.InventoryAdjustmentMapper;
import com.alshamel.mall.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryAdjustmentService {

    private final BatchMapper batchMapper;
    private final AdjustmentReasonMapper reasonMapper;
    private final UserMapper userMapper;
    private final InventoryAdjustmentMapper inventoryAdjustmentMapper;

    @Transactional
    public InventoryAdjustmentView create(CreateAdjustmentRequest request, String operatorUsername) {
        User operator = resolveOperator(operatorUsername);
        return applyOne(request.getBatchId(), request.getReasonId(),
                request.getNewQuantity(), request.getNote(), operator.getId());
    }

    /** 批量调整：逐条复用同一套校验规则，整体一个事务，任一失败全部回滚 */
    @Transactional
    public List<InventoryAdjustmentView> createBatch(BatchAdjustmentRequest request, String operatorUsername) {
        User operator = resolveOperator(operatorUsername);
        List<BatchAdjustmentRequest.Item> items = request.getItems();

        // 同一请求中批次不允许重复（否则乐观锁会冲突）
        Set<Long> batchIds = items.stream()
                .map(BatchAdjustmentRequest.Item::getBatchId)
                .collect(Collectors.toSet());
        if (batchIds.size() != items.size()) {
            throw BusinessException.badRequest("Duplicate batch in request");
        }

        List<InventoryAdjustmentView> result = new ArrayList<>();
        for (BatchAdjustmentRequest.Item item : items) {
            result.add(applyOne(item.getBatchId(), item.getReasonId(),
                    item.getNewQuantity(), item.getNote(), operator.getId()));
        }
        return result;
    }

    public List<InventoryAdjustmentView> listAll() {
        return inventoryAdjustmentMapper.selectAllWithDetails();
    }

    public InventoryAdjustmentView getById(Long id) {
        InventoryAdjustmentView view = inventoryAdjustmentMapper.selectWithDetailsById(id);
        if (view == null) {
            throw BusinessException.notFound("Adjustment not found: " + id);
        }
        return view;
    }

    /**
     * 单条调整核心逻辑（单次与批量共用）：
     * 校验批次存在 -> 原因存在/启用/适用于库存调整 -> 记录旧/新数量与差值 -> 乐观锁安全更新
     */
    private InventoryAdjustmentView applyOne(Long batchId, Long reasonId, Integer newQuantity,
                                             String note, Long userId) {
        // 1. 批次存在
        Batch batch = batchMapper.findById(batchId);
        if (batch == null) {
            throw BusinessException.notFound("Batch not found: " + batchId);
        }

        // 2. 原因存在
        AdjustmentReason reason = reasonMapper.findById(reasonId);
        if (reason == null) {
            throw BusinessException.notFound("Reason not found: " + reasonId);
        }
        // 3. 原因启用
        if (!reason.isActive()) {
            throw BusinessException.badRequest("Reason is not active: " + reason.getCode());
        }
        // 4. 原因适用于库存调整
        if (!"INVENTORY".equals(reason.getType())) {
            throw BusinessException.badRequest("Reason is not valid for inventory adjustments: " + reason.getCode());
        }

        int oldQuantity = batch.getQuantity();
        int diff = newQuantity - oldQuantity;

        // 5. 记录调整
        InventoryAdjustment adjustment = new InventoryAdjustment();
        adjustment.setBatchId(batch.getId());
        adjustment.setReasonId(reason.getId());
        adjustment.setUserId(userId);
        adjustment.setOldQuantity(oldQuantity);
        adjustment.setNewQuantity(newQuantity);
        adjustment.setQuantityDiff(diff);
        adjustment.setNote(note);
        inventoryAdjustmentMapper.insert(adjustment);

        // 6. 乐观锁安全更新批次数量
        int updated = batchMapper.updateQuantityIfUnchanged(batch.getId(), oldQuantity, newQuantity);
        if (updated != 1) {
            throw BusinessException.badRequest(
                    "Batch quantity was changed concurrently, please retry: " + batch.getBatchNo());
        }

        return inventoryAdjustmentMapper.selectWithDetailsById(adjustment.getId());
    }

    private User resolveOperator(String username) {
        User operator = userMapper.findByUsername(username);
        if (operator == null) {
            throw BusinessException.badRequest("Unknown operator: " + username);
        }
        return operator;
    }
}
