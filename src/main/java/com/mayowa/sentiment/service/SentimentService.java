package com.mayowa.sentiment.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SentimentService {

    private final WebClient webClient;

    public SentimentService(@Value("${huggingface.api.url}") String apiUrl,
                            @Value("${HF_API_TOKEN}") String apiToken) {
        this.webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiToken)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Map<String, Object> analyzeMessages(List<String> messages) {
        int positive = 0, negative = 0, neutral = 0;
        String needsAttention = null;

        for (String message : messages) {
            Map<String, Object> result = callHuggingFace(message);
            String label = (String) result.get("label");

            switch (label) {
                case "POSITIVE":
                    positive++;
                    break;
                case "NEGATIVE":
                    negative++;
                    if (needsAttention == null) needsAttention = message;
                    break;
                default:
                    neutral++;
            }
        }

        int total = messages.size();
        double positivePct = total > 0 ? (positive * 100.0 / total) : 0;
        double neutralPct = total > 0 ? (neutral * 100.0 / total) : 0;
        double negativePct = total > 0 ? (negative * 100.0 / total) : 0;

        Map<String, Object> summary = new HashMap<>();
        summary.put("positive", positive);
        summary.put("neutral", neutral);
        summary.put("negative", negative);
        summary.put("needsAttention", needsAttention);
        summary.put("positivePct", String.format("%.1f", positivePct));
        summary.put("neutralPct", String.format("%.1f", neutralPct));
        summary.put("negativePct", String.format("%.1f", negativePct));
        return summary;
    }

    private Map<String, Object> callHuggingFace(String text) {
        try {
            Mono<List<List<Map<String, Object>>>> responseMono = webClient.post()
                    .bodyValue(Map.of("inputs", text))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<List<Map<String, Object>>>>() {});

            List<List<Map<String, Object>>> outerList = responseMono.block();

            if (outerList != null && !outerList.isEmpty()) {
                List<Map<String, Object>> predictions = outerList.get(0);
                if (predictions != null && !predictions.isEmpty()) {
                    // Pick the highest scoring label
                    Map<String, Object> bestPrediction = predictions.get(0);
                    String label = ((String) bestPrediction.get("label")).toUpperCase();
                    Double score = (Double) bestPrediction.get("score");

                    Map<String, Object> result = new HashMap<>();
                    result.put("label", label);
                    result.put("score", score);
                    return result;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // fallback
        Map<String, Object> fallback = new HashMap<>();
        fallback.put("label", "NEUTRAL");
        fallback.put("score", 0.0);
        return fallback;
    }
}
