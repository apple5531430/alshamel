package com.alshamel.mall.service;

import com.alshamel.mall.entity.Product;
import com.alshamel.mall.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper productMapper;

    public List<Product> listAll() {
        return productMapper.findAll();
    }
}
