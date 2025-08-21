package com.example.reewild.vision;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MockVisionClient implements VisionClient {
    @Override
    public List<String> extractIngredients(byte[] ignored, String mime) {
        // Pretend we looked at the image and found these:
        return List.of("Rice", "Chicken", "Onion", "Tomato", "Spices", "Oil");
    }
}