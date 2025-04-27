package com.resumeanalyzer.resume_analyzer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResumeAnalyzerService {

    @Autowired
    private GeminiService geminiService;

    public String analyzeResume(String extractedText) {
        return geminiService.generateFeedback(extractedText);
    }
}
