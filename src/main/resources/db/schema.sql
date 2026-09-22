-- =============================================================
-- 库存批次数量调整 Demo - 建表语句 (MySQL 8)
-- =============================================================

-- 用户表：存储用户名与密码（密码为 BCrypt 哈希，绝不存明文）
CREATE TABLE IF NOT EXISTS users (
    id          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL,
    password    VARCHAR(100) NOT NULL,
    role        VARCHAR(30)  NOT NULL DEFAULT 'USER',
    enabled     TINYINT(1)   NOT NULL DEFAULT 1,
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_users_username (username)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- 仓库表
CREATE TABLE IF NOT EXISTS warehouse (
    id          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    code        VARCHAR(30)  NOT NULL,
    name        VARCHAR(100) NOT NULL,
    location    VARCHAR(200),
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_warehouse_code (code)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- 产品表
CREATE TABLE IF NOT EXISTS product (
    id          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    sku         VARCHAR(50)  NOT NULL,
    name        VARCHAR(100) NOT NULL,
    unit        VARCHAR(20),
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_product_sku (sku)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- 批次表：产品 + 仓库 + 当前数量
CREATE TABLE IF NOT EXISTS batch (
    id           BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    batch_no     VARCHAR(50) NOT NULL,
    product_id   BIGINT UNSIGNED NOT NULL,
    warehouse_id BIGINT UNSIGNED NOT NULL,
    quantity     INT          NOT NULL DEFAULT 0,
    created_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_batch_no (batch_no),
    KEY idx_batch_product (product_id),
    KEY idx_batch_warehouse (warehouse_id),
    CONSTRAINT fk_batch_product   FOREIGN KEY (product_id)   REFERENCES product (id),
    CONSTRAINT fk_batch_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouse (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- 预定义调整原因表：type 标识可用于哪种调整（INVENTORY / OTHER）
CREATE TABLE IF NOT EXISTS adjustment_reason (
    id          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    code        VARCHAR(50)  NOT NULL,
    name        VARCHAR(100) NOT NULL,
    type        VARCHAR(30)  NOT NULL,
    is_active   TINYINT(1)   NOT NULL DEFAULT 1,
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_reason_code (code)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- 库存调整记录表
CREATE TABLE IF NOT EXISTS inventory_adjustment (
    id            BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    batch_id      BIGINT UNSIGNED NOT NULL,
    reason_id     BIGINT UNSIGNED NOT NULL,
    user_id       BIGINT UNSIGNED NOT NULL,
    old_quantity  INT          NOT NULL,
    new_quantity  INT          NOT NULL,
    quantity_diff INT          NOT NULL,
    note          VARCHAR(500),
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_adjustment_batch (batch_id),
    KEY idx_adjustment_reason (reason_id),
    CONSTRAINT fk_adjustment_batch  FOREIGN KEY (batch_id)  REFERENCES batch (id),
    CONSTRAINT fk_adjustment_reason FOREIGN KEY (reason_id) REFERENCES adjustment_reason (id),
    CONSTRAINT fk_adjustment_user   FOREIGN KEY (user_id)   REFERENCES users (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;
