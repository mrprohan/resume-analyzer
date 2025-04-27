package com.resumeanalyzer.resume_analyzer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    public String generateFeedback(String resumeText) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> textPart = Map.of("text", "Give smart resume feedback with improvement suggestions:\n\n" + resumeText);
        Map<String, Object> request = Map.of("contents", List.of(Map.of("parts", List.of(textPart))));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String fullUrl = apiUrl + "?key=" + apiKey;

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(fullUrl, entity, Map.class);
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.getBody().get("candidates");
            Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
            String result = parts.get(0).get("text").toString();

            System.out.println("Gemini says: " + result); // optional debug
            return result;

        } catch (Exception e) {
            return "⚠️ Gemini API failed: " + e.getMessage();
        }
    }
}
