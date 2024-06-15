package it.polimi.ingsw.chat;

import java.io.Serializable;

/**
 * Class that represents a chat message
 */
public class ChatMessageData implements Serializable {
    private String recipient;
    private final String sender;

    private final String content;

    /**
     * Constructor
     * @param sender: the sender of the message
     * @param recipient: the recipient of the message
     * @param content: the content of the message
     */
    public ChatMessageData(String sender, String recipient, String content) {
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
    }

    /**
     * Setter for the recipient of the message
     * @param recipient the recipient of the message
     */
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    /**
     * Getter for the recipient of the message
     * @return the recipient of the message
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * Getter for the sender of the message
     * @return the sender of the message
     */
    public String getSender() {
        return sender;
    }

    /**
     * Getter for the content of the message
     * @return the content of the message
     */
    public String getContent() {
        return content;
    }

/**
     * String representation of the message
     * @return the string representation of the message
     */
    @Override
    public String toString() {
        return String.format("[%s@%s]: %s", sender, recipient, content);
    }
}
