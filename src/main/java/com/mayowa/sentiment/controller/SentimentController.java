package com.mayowa.sentiment.controller;

import com.mayowa.sentiment.service.SentimentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sentiment")
public class SentimentController {

    private final SentimentService sentimentService;

    public SentimentController(SentimentService sentimentService) {
        this.sentimentService = sentimentService;
    }

    @PostMapping("/analyze")
    public Map<String, Object> analyzeMessages(@RequestBody Map<String, List<String>> payload) {
        List<String> messages = payload.get("messages");
        return sentimentService.analyzeMessages(messages); // ✅ fixed method call
    }
}
