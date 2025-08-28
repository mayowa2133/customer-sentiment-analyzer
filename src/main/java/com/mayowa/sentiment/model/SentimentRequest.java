package com.mayowa.sentiment.model;

import java.util.List;

public class SentimentRequest {
    private List<String> messages;

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}
