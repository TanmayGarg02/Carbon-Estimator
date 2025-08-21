package com.example.reewild.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodEstimateRequest {
    private String dishName;
    private int servings = 1;    // new field, default 1

}