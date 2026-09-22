package com.alshamel.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.alshamel.mall.mapper")
public class AlshamelApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlshamelApplication.class, args);
    }
}
