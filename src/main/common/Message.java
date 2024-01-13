package main.common;

import java.time.ZonedDateTime;

// текст сообщения и
// дату и время отправки сообщения с временной зоной
public class Message {
    private final String text;
    private ZonedDateTime sentAt;

    public Message(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public ZonedDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(ZonedDateTime sentAt) {
        this.sentAt = sentAt;
    }
}