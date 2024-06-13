package sbu;

import java.time.LocalDateTime;
import java.util.UUID;
public class TextMessage implements Message {
    private String text;
    private final String author;
    private LocalDateTime date;

    public TextMessage(String text, String author){
        this.text = text;
        this.author = author;
        this.date = LocalDateTime.now();
    }

    public TextMessage(String text, String author, LocalDateTime t) {
        this.text = text;
        this.author = author;
        this.date = t;
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public String getAuthor() {
        return this.author;
    }

    @Override
    public LocalDateTime getDate() {
        return this.date;
    }

    @Override
    public UUID getMessageID() {
        return null;
    }
}
