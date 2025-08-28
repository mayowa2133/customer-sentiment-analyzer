package com.mayowa.sentiment.model;

public class SentimentSummary {
    private int positive;
    private int neutral;
    private int negative;
    private String needsAttention;

    public SentimentSummary(int positive, int neutral, int negative, String needsAttention) {
        this.positive = positive;
        this.neutral = neutral;
        this.negative = negative;
        this.needsAttention = needsAttention;
    }

    public int getPositive() { return positive; }
    public int getNeutral() { return neutral; }
    public int getNegative() { return negative; }
    public String getNeedsAttention() { return needsAttention; }
}
