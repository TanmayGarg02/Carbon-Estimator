package com.example.reewild.vision;

import com.example.reewild.config.OpenAIProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Component
public class OpenAIVisionClient implements VisionClient {
    private final WebClient webClient;
    private final ObjectMapper mapper = new ObjectMapper();
    private final OpenAIProperties props;

    public OpenAIVisionClient(OpenAIProperties props) {
        this.props = props;
        this.webClient = WebClient.builder()
                .baseUrl(props.getBaseUrl())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + props.getApiKey())
                .build();
    }

    @Override
    public List<String> extractIngredients(byte[] imageBytes, String mimeType) throws Exception {
        if (!props.isEnabled() || props.getApiKey() == null || props.getApiKey().isBlank())
            throw new IllegalStateException("OpenAI disabled or API key missing");

        String b64 = Base64.getEncoder().encodeToString(imageBytes);
        Map<String, Object> textPart = Map.of(
                "type", "text",
                "text", "Identify the dish or list 6-10 likely ingredients. " +
                        "Return ONLY a JSON array of strings."
        );
        Map<String, Object> imagePart = Map.of(
                "type", "image_url",
                "image_url", Map.of("url", "data:" + mimeType + ";base64," + b64)
        );
        Map<String, Object> userMessage = Map.of(
                "role", "user",
                "content", List.of(textPart, imagePart)
        );

        Map<String, Object> body = new HashMap<>();
        body.put("model", props.getVisionModel());
        body.put("temperature", 0.2);
        body.put("messages", List.of(userMessage));

        String json = webClient.post()
                .uri("/chat/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JsonNode root = mapper.readTree(json);
        String content = root.path("choices").get(0).path("message").path("content").asText("[]");
        JsonNode arr = mapper.readTree(content);
        List<String> ingredients = new ArrayList<>();
        arr.forEach(n -> ingredients.add(n.asText()));
        return ingredients;
    }
}