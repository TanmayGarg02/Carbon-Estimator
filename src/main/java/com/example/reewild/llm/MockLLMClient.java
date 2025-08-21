package com.example.reewild.llm;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component("mockLLMClient")
public class MockLLMClient implements LLMClient {
    private static final Map<String, List<String>> SAMPLE = Map.of(
            "chicken biryani", List.of("Rice", "Chicken", "Onion", "Tomato", "Yogurt", "Spices", "Oil"),
            "margherita pizza", List.of("Wheat Flour", "Tomato", "Mozzarella", "Olive Oil", "Basil")
    );

    @Override
    public List<String> extractIngredients(String dish) {
        return SAMPLE.getOrDefault(dish.toLowerCase(),
                List.of("Rice", "Vegetables", "Oil", "Spices"));
    }
}