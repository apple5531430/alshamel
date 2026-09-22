package com.alshamel.mall.controller;

import com.alshamel.mall.common.ApiResponse;
import com.alshamel.mall.entity.Product;
import com.alshamel.mall.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ApiResponse<List<Product>> list() {
        return ApiResponse.success(productService.listAll());
    }
}
