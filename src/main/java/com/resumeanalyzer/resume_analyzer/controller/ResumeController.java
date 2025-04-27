package com.resumeanalyzer.resume_analyzer.controller;

import com.resumeanalyzer.resume_analyzer.model.Resume;
import com.resumeanalyzer.resume_analyzer.service.ResumeFirebaseService;
import com.resumeanalyzer.resume_analyzer.service.ResumeAnalyzerService;
import com.resumeanalyzer.resume_analyzer.dto.UploadResponse;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    @Autowired
    private ResumeFirebaseService resumeFirebaseService;

    @Autowired
    private ResumeAnalyzerService resumeAnalyzerService;

    @PostMapping("/upload")
    public UploadResponse uploadResume(@RequestParam("file") MultipartFile file)throws Exception {
        try {
            // Parse the file using Apache Tika
            BodyContentHandler handler = new BodyContentHandler(-1);
            AutoDetectParser parser = new AutoDetectParser();
            Metadata metadata = new Metadata();

            parser.parse(file.getInputStream(), handler, metadata);
            String extractedText = handler.toString();

            Resume resume = new Resume();
            resume.setOriginalFileName(file.getOriginalFilename());
            resume.setUploadedDateTime(LocalDateTime.now());
            resume.setExtractedText(extractedText);

            // Analyze the resume text
            String feedback = resumeAnalyzerService.analyzeResume(extractedText);
            resume.setFeedback(feedback);

            // Save to Firebase
            String documentId = resumeFirebaseService.saveResume(resume);

            return new UploadResponse(
                    documentId,
                    "✅ Resume uploaded and analyzed successfully!",
                    "http://localhost:8080/api/resumes/" + documentId
            );

        } catch (IOException | TikaException | SAXException e) {
            e.printStackTrace();
            return new UploadResponse(
                    null,
                    "❌ Error uploading resume: " + e.getMessage(),
                    null
            );
        }
    }

    @GetMapping("/{id}")
    public Resume getResumeById(@PathVariable String id) throws Exception {
        return resumeFirebaseService.getResumeById(id);
    }
}
