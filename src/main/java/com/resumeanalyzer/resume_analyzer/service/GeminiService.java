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

        String prompt = """
            You are a professional resume reviewer. Carefully review ONLY the skills mentioned in the following resume text.
            Give feedback on:
            - Strength of technical skills
            - Mentioned soft skills
            - What could be improved (e.g., missing modern tools or soft skills)
            
            Respond in plain text with clear, professional suggestions.

            Resume:
            \"\"\"
            %s
            \"\"\"
            """.formatted(resumeText);

        Map<String, Object> textPart = Map.of("text", prompt);
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
            return parts.get(0).get("text").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "⚠️ Gemini API failed: " + e.getMessage();
        }
    }
}
