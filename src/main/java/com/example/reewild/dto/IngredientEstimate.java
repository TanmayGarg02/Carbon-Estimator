package com.example.reewild.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientEstimate {
    private String ingredient;
    private double carbon_kg;
}
