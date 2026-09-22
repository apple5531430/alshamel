package com.alshamel.mall;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 轻量单元测试：验证 data.sql 中预置的 BCrypt 密码哈希与明文密码匹配。
 * 不加载 Spring 上下文，因此无需数据库即可运行。
 */
class AlshamelApplicationTests {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Test
    void seededAdminPasswordMatches() {
        String hash = "$2b$10$yOmDP2KyAXLpkO4jqu1TPOZlrbmiB7JIlxuB5g1M6mfFu86Fwe6pC";
        assertTrue(encoder.matches("admin123", hash));
    }

    @Test
    void seededUserPasswordMatches() {
        String hash = "$2b$10$25xI1Il6ALGG/bWNOXq.ku0l1ExmWfTif0Y13awnzwc9EAB52vjdi";
        assertTrue(encoder.matches("user123", hash));
    }
}
