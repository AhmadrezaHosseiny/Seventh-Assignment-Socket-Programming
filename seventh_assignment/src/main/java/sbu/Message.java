package sbu;

import java.util.UUID;
import java.time.LocalDateTime;
public interface Message {
    public String getText();
    public String getAuthor();
    public LocalDateTime getDate();
    public UUID getMessageID();

}
