package com.alshamel.mall.mapper;

import com.alshamel.mall.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {

    @Select("SELECT id, username, password, role, enabled, created_at FROM users WHERE username = #{username}")
    User findByUsername(@Param("username") String username);
}
