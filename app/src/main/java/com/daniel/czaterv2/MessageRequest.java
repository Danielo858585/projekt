package com.daniel.czaterv2;

/**
 * Created by Daniel on 02.01.2017.
 */
public class MessageRequest {
    private String author;
    private String textMessage;
    private String tokenContent;

    public MessageRequest() {
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public String getTokenContent() {
        return tokenContent;
    }

    public void setTokenContent(String tokenContent) {
        this.tokenContent = tokenContent;
    }
}
