package com.example.reewild.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodEstimateResponse {
    private String dish;
    private int servings;
    private double footprintPerServing;
    private double totalFootprint;
    private List<IngredientEstimate> ingredients;
}
