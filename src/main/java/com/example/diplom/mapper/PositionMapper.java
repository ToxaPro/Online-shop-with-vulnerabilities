package com.example.diplom.mapper;

import com.example.diplom.dto.PositionDto;
import com.example.diplom.entity.Position;
import org.springframework.stereotype.Service;

@Service
public class PositionMapper {
    public static PositionDto mapPositionToPositionDto(Position product) {
        return PositionDto.builder()
            .product(product.getProduct())
            .amount(product.getAmount())
            .build();
    }
}
