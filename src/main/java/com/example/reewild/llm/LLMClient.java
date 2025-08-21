package com.example.reewild.llm;

import java.util.List;

public interface LLMClient {
    List<String> extractIngredients(String dish) throws Exception;
}