package com.daniel.czaterv2;

import java.io.Serializable;

/**
 * Created by Daniel on 01.01.2017.
 */
public class MessageResponse implements Serializable {
    String author;
    String textMessage;
    String time;

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public MessageResponse(String author, String textMessage, String time) {

        this.author = author;
        this.textMessage = textMessage;
        this.time = time;
    }

    public MessageResponse() {

    }
}
