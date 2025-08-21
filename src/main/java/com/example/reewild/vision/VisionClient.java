package com.example.reewild.vision;

import java.util.List;

public interface VisionClient {
    List<String> extractIngredients(byte[] imageBytes, String mime) throws Exception;
}