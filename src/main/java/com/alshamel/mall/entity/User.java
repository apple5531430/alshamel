package com.alshamel.mall.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String username;
    /** BCrypt 哈希，绝不明文 */
    private String password;
    private String role;
    private Boolean enabled;
    private LocalDateTime createdAt;
}
