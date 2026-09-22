-- =============================================================
-- 库存批次数量调整 Demo - 演示数据
-- 使用 INSERT IGNORE 保证重复执行（重启）时幂等
-- =============================================================

-- 演示用户（密码为 BCrypt 哈希，非明文）：
--   admin / admin123  (角色 ADMIN)
--   user  / user123   (角色 USER)
INSERT IGNORE INTO users (id, username, password, role, enabled) VALUES
    (1, 'admin', '$2b$10$yOmDP2KyAXLpkO4jqu1TPOZlrbmiB7JIlxuB5g1M6mfFu86Fwe6pC', 'ADMIN', 1),
    (2, 'user',  '$2b$10$25xI1Il6ALGG/bWNOXq.ku0l1ExmWfTif0Y13awnzwc9EAB52vjdi', 'USER',  1);

-- 仓库
INSERT IGNORE INTO warehouse (id, code, name, location) VALUES
    (1, 'WH-MAIN', 'Main Warehouse', 'Building A'),
    (2, 'WH-BACK', 'Backup Warehouse', 'Building B');

-- 产品
INSERT IGNORE INTO product (id, sku, name, unit) VALUES
    (1, 'SKU-1001', 'Wireless Mouse', 'pcs'),
    (2, 'SKU-1002', 'Mechanical Keyboard', 'pcs'),
    (3, 'SKU-1003', 'USB-C Cable', 'pcs');

-- 批次（示例：BATCH-2024-0001 系统数量 100）
INSERT IGNORE INTO batch (id, batch_no, product_id, warehouse_id, quantity) VALUES
    (1, 'BATCH-2024-0001', 1, 1, 100),
    (2, 'BATCH-2024-0002', 2, 1, 250),
    (3, 'BATCH-2024-0003', 3, 2, 500);

-- 预定义调整原因（固定 4 个，不可新增/删除，仅可启用/禁用）
INSERT IGNORE INTO adjustment_reason (id, code, name, type, is_active) VALUES
    (1, 'PHYSICAL_COUNT', 'Physical count correction', 'INVENTORY', 1),
    (2, 'DAMAGED',        'Damaged items',            'INVENTORY', 1),
    (3, 'MISSING',        'Missing items',            'INVENTORY', 1),
    (4, 'DATA_ENTRY',     'Data entry correction',    'INVENTORY', 1);
