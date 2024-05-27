package it.polimi.ingsw.chat;

import java.io.Serializable;

public class ChatMessageData implements Serializable {
    private String recipient;
    private final String sender;

    private final String content;

    public ChatMessageData(String sender, String recipient, String content) {
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return String.format("[%s@%s]: %s", sender, recipient, content);
    }
}
