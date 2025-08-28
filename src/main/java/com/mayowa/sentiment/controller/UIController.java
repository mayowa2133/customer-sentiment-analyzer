package com.mayowa.sentiment.controller;

import com.mayowa.sentiment.service.SentimentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class UIController {

    private final SentimentService sentimentService;

    public UIController(SentimentService sentimentService) {
        this.sentimentService = sentimentService;
    }

    @GetMapping("/")
    public String home() {
        return "index"; // loads index.html from templates
    }

    @PostMapping("/analyze")
    public String analyze(@RequestParam("messages") String messages, Model model) {
        // Split input by newlines
        List<String> messageList = List.of(messages.split("\\r?\\n"));
        Map<String, Object> result = sentimentService.analyzeMessages(messageList);

        model.addAttribute("result", result);
        model.addAttribute("messages", messages);
        return "index";
    }
}
