package sbu;

import java.util.UUID;
import java.time.LocalDateTime;
public class FileMessage implements Message{
    private final String url;
    private final UUID fileId;
    private final String text;
    private final String author;
    private final LocalDateTime date;
    public FileMessage(String url, String text, String author){
        this.url = url;
        this.text = text;
        this.author = author;
        fileId = UUID.randomUUID();
        this.date = LocalDateTime.now();
    }
    public FileMessage(String text, String author, LocalDateTime time, UUID messageID) {
        this.url = "";
        this.date = time;
        this.text = text;
        this.author = author;
        this.fileId = messageID;
    }
    public String getUrl() {
        return this.url;
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
    public UUID getMessageID() {
        return this.fileId;
    }
    @Override
    public LocalDateTime getDate() {
        return this.date;
    }
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof FileMessage)
            return this.fileId.equals(((FileMessage) obj).fileId);
        return false;
    }
}
