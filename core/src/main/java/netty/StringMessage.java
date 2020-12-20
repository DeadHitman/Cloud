package netty;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class StringMessage extends AbstractMessage {

    private LocalDateTime sendAt;
    private String message;
    private String author;

    public StringMessage() {
    }

    public StringMessage(String message, String author) {
        this.message = message;
        this.author = author;
        sendAt = LocalDateTime.now();
    }

    public LocalDateTime getSendAt() {
        return sendAt;
    }

    public void setSendAt(LocalDateTime sendAt) {
        this.sendAt = sendAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "StringMessage{" +
                "sendAt=" + sendAt +
                ", message='" + message + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
