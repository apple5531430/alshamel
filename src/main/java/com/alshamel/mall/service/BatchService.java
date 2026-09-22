package com.alshamel.mall.service;

import com.alshamel.mall.dto.BatchView;
import com.alshamel.mall.mapper.BatchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BatchService {

    private final BatchMapper batchMapper;

    public List<BatchView> listAll() {
        return batchMapper.findAllWithDetails();
    }
}
