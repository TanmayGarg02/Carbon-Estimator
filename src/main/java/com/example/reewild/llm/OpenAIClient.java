package com.example.reewild.llm;

import com.example.reewild.config.OpenAIProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("openAiLLMClient")
public class OpenAIClient implements LLMClient {
    private final WebClient webClient;
    private final ObjectMapper mapper = new ObjectMapper();
    private final OpenAIProperties props;

    public OpenAIClient(OpenAIProperties props) {
        this.props = props;
        this.webClient = WebClient.builder()
                .baseUrl(props.getBaseUrl())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + props.getApiKey())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public List<String> extractIngredients(String dish) throws Exception {
        if (!props.isEnabled() || props.getApiKey() == null || props.getApiKey().isBlank()) {
            throw new IllegalStateException("OpenAI disabled or API key missing");
        }

        Map<String, Object> systemMsg = Map.of(
                "role", "system",
                "content", "You are a culinary assistant. Extract 6-10 core ingredients for the dish. " +
                        "Return ONLY a JSON array of strings (no comments).");

        Map<String, Object> userMsg = Map.of("role", "user", "content", dish);

        Map<String, Object> body = new HashMap<>();
        body.put("model", props.getChatModel());
        body.put("temperature", 0.2);
        body.put("messages", List.of(systemMsg, userMsg));

        String json = webClient.post()
                .uri("/chat/completions")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JsonNode root = mapper.readTree(json);
        JsonNode choices = root.path("choices");
        if (!choices.isArray() || choices.isEmpty()) {
            throw new IllegalStateException("No choices returned from OpenAI: " + json);
        }

        String content = choices.get(0).path("message").path("content").asText("[]");

        List<String> ingredients = new ArrayList<>();
        try {
            JsonNode arr = mapper.readTree(content);
            if (arr.isArray()) {
                arr.forEach(n -> ingredients.add(n.asText()));
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to parse ingredients JSON: " + content, e);
        }

        return ingredients;
    }
}
