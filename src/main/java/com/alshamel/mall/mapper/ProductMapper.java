package com.alshamel.mall.mapper;

import com.alshamel.mall.entity.Product;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ProductMapper {

    @Select("SELECT id, sku, name, unit, created_at FROM product ORDER BY id")
    List<Product> findAll();
}
