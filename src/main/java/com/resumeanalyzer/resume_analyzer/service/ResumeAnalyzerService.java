package com.resumeanalyzer.resume_analyzer.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResumeAnalyzerService {

    @Autowired
    private GeminiService geminiService;

    public String generateSkillFeedback(String extractedText) {
        String prompt = """
            You are a professional resume reviewer. Extract and analyze ONLY skills from the following resume text.

            Return your response in the following JSON format:
            {
              "technical_skills": ["..."],
              "soft_skills": ["..."],
              "strengths": "short paragraph",
              "improvements": "short paragraph"
            }

            Do not include formatting or grammar tips.

            Resume:
            \"\"\"
            %s
            \"\"\"
            """.formatted(extractedText);

        return geminiService.generateFeedback(extractedText);
    }
}
