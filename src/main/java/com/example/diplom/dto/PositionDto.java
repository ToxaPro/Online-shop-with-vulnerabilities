package com.example.diplom.dto;

import com.example.diplom.entity.Product;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PositionDto {
    Product product;

    int amount;
}
