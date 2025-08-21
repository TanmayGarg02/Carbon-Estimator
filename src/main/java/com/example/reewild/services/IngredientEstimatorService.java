package com.example.reewild.services;

import com.example.reewild.config.OpenAIProperties;
import com.example.reewild.llm.LLMClient;
import com.example.reewild.llm.MockLLMClient;
import com.example.reewild.llm.OpenAIClient;
import com.example.reewild.vision.MockVisionClient;
import com.example.reewild.vision.OpenAIVisionClient;
import com.example.reewild.vision.VisionClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientEstimatorService {
    private final OpenAIProperties props;
    private final OpenAIClient openAIClient;
    private final OpenAIVisionClient openAIVisionClient;
    private final MockLLMClient mockLLMClient;
    private final MockVisionClient mockVisionClient;

    private LLMClient textClient;
    private VisionClient visionClient;

    @PostConstruct
    private void init() {
        if (props.isEnabled()) {
            this.textClient = openAIClient;
            this.visionClient = openAIVisionClient;
        } else {
            this.textClient = mockLLMClient;
            this.visionClient = mockVisionClient;
        }
    }

    public List<String> fromDish(String dish) throws Exception {
        return textClient.extractIngredients(dish);
    }

    public List<String> fromImage(byte[] bytes, String mime) throws Exception {
        return visionClient.extractIngredients(bytes, mime);
    }
}
