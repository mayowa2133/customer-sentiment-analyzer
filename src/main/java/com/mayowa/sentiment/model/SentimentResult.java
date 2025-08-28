package com.mayowa.sentiment.model;

public class SentimentResult {
    private String label;
    private double confidence;

    public SentimentResult(String label, double confidence) {
        this.label = label;
        this.confidence = confidence;
    }

    public String getLabel() {
        return label;
    }

    public double getConfidence() {
        return confidence;
    }
}
