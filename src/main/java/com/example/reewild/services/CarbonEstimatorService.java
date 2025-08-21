package com.example.reewild.services;

import com.example.reewild.dto.IngredientEstimate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class CarbonEstimatorService {

    // Per ingredient estimation
    public List<IngredientEstimate> estimatePerIngredient(List<String> ingredients) {
        List<IngredientEstimate> list = new ArrayList<>();
        for (String ing : ingredients) {
            double intensity = CarbonDatabase.intensityFor(ing); // kg CO2e / kg
            double grams = CarbonDatabase.typicalGrams(ing);     // grams heuristic
            double carbonKg = (intensity * grams) / 1000.0;
            carbonKg = round(carbonKg, 2);

            list.add(new IngredientEstimate(capitalize(ing), carbonKg));
        }
        return list;
    }

    // Total for list
    public double total(List<IngredientEstimate> items) {
        return round(items.stream()
                .mapToDouble(IngredientEstimate::getCarbon_kg)
                .sum(), 2);
    }

    // Total per serving (essentially same as total, but extracted for clarity)
    public double estimatePerServing(List<IngredientEstimate> ingredients) {
        return total(ingredients);
    }

    private static double round(double v, int scale) {
        return BigDecimal.valueOf(v).setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }

    private static String capitalize(String s) {
        if (s == null || s.isBlank()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
