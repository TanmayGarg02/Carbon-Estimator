package com.example.reewild.controller;

import com.example.reewild.dto.IngredientEstimate;
import com.example.reewild.services.CarbonEstimatorService;
import com.example.reewild.services.IngredientEstimatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/estimate")
@RequiredArgsConstructor
public class EstimationController {

    private final IngredientEstimatorService ingredientEstimatorService;
    private final CarbonEstimatorService carbonEstimatorService;

    /**
     * Estimate carbon impact from a dish name / recipe description
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> estimateFromText(@RequestParam String dish) {
        try {
            List<String> ingredients = ingredientEstimatorService.fromDish(dish);
            List<IngredientEstimate> estimates = carbonEstimatorService.estimatePerIngredient(ingredients);
            double total = carbonEstimatorService.total(estimates);

            Map<String, Object> response = new HashMap<>();
            response.put("ingredients", estimates);
            response.put("totalCarbonKg", total);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Estimate carbon impact from a food image
     */
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> estimateFromImage(@RequestParam("file") MultipartFile file) {
        try {
            String mime = file.getContentType();
            List<String> ingredients = ingredientEstimatorService.fromImage(file.getBytes(), mime);
            List<IngredientEstimate> estimates = carbonEstimatorService.estimatePerIngredient(ingredients);
            double total = carbonEstimatorService.total(estimates);

            Map<String, Object> response = new HashMap<>();
            response.put("ingredients", estimates);
            response.put("totalCarbonKg", total);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
