package com.example.test.dto;

public class ReceiveMessageDto {


    private Content text;

    public Content getText() {
        return text;
    }

    public void setText(Content text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "ReceiveMessageDto{" +
                "text=" + text +
                '}';
    }
}
