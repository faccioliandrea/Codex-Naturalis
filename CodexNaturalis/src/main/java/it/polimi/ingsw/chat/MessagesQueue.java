package it.polimi.ingsw.chat;

import java.util.LinkedList;
import java.util.List;

/**
 * Class that represents a queue of chat messages
 */
public class MessagesQueue extends LinkedList<ChatMessageData> {
    private final int maxSize;

    /**
     * Default constructor
     */
    public MessagesQueue() {
        this.maxSize = -1;
    }

    /**
     * Constructor
     * @param maxSize: the maximum size of the queue
     */
    public MessagesQueue(int maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * Constructor
     * @param list: the list of messages
     * @param maxSize: the maximum size of the queue
     */
    public MessagesQueue(List<ChatMessageData> list, int maxSize) {
        super(list);
        this.maxSize = maxSize;
        while (size() > this.maxSize) {
            remove();
        }
    }

    /**
     * Add a message to the queue
     * @param o: the message to add
     * @return true if the message was added, false otherwise
     */
    @Override
    public synchronized boolean add(ChatMessageData o) {
        boolean added = super.add(o);
        while (this.maxSize > 0 && added && size() > this.maxSize) {
            remove();
        }
        return added;
    }
}
