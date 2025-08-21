package com.example.reewild.services;

import java.util.Map;

public class CarbonDatabase {
    public static final Map<String, Double> INTENSITY = Map.ofEntries(
            Map.entry("beef", 27.0), Map.entry("lamb", 39.0), Map.entry("pork", 12.0),
            Map.entry("chicken", 6.9), Map.entry("fish", 5.0), Map.entry("egg", 4.8),
            Map.entry("cheese", 13.5), Map.entry("milk", 3.0), Map.entry("yogurt", 2.2),
            Map.entry("rice", 4.0), Map.entry("wheat", 2.5), Map.entry("bread", 2.0),
            Map.entry("potato", 0.3), Map.entry("vegetable", 0.6), Map.entry("tomato", 1.4),
            Map.entry("onion", 0.5), Map.entry("oil", 6.0), Map.entry("olive oil", 6.0),
            Map.entry("spice", 2.0), Map.entry("spices", 2.0), Map.entry("lentil", 0.9),
            Map.entry("bean", 1.0), Map.entry("mozzarella", 10.0)
    );

    // Typical serving mass heuristics (grams)
    public static double typicalGrams(String name) {
        String s = name.toLowerCase();
        if (s.contains("beef") || s.contains("lamb") || s.contains("pork") || s.contains("chicken") || s.contains("fish"))
            return 150;
        if (s.contains("rice") || s.contains("bread") || s.contains("wheat") || s.contains("pasta"))
            return 180;
        if (s.contains("cheese") || s.contains("mozzarella"))
            return 30;
        if (s.contains("egg")) return 60;
        if (s.contains("oil")) return 15;
        if (s.contains("spice")) return 5;
        if (s.contains("tomato") || s.contains("onion"))
            return 80;
        return 100; // fallback
    }

    public static double intensityFor(String name) {
        String s = name.toLowerCase();
        // crude keyword matching
        for (String k : INTENSITY.keySet()) {
            if (s.contains(k)) return INTENSITY.get(k);
        }
        if (s.contains("veg")) return INTENSITY.get("vegetable");
        return 1.0; // generic fallback
    }
}
