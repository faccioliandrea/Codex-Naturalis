package it.polimi.ingsw.chat;

import java.util.LinkedList;
import java.util.List;

public class MessagesQueue extends LinkedList<ChatMessageData> {
    private final int maxSize;

    public MessagesQueue() {
        this.maxSize = -1;
    }

    public MessagesQueue(int maxSize) {
        this.maxSize = maxSize;
    }

    public MessagesQueue(List<ChatMessageData> list, int maxSize) {
        super(list);
        this.maxSize = maxSize;
        while (size() > this.maxSize) {
            remove();
        }
    }

    @Override
    public synchronized boolean add(ChatMessageData o) {
        boolean added = super.add(o);
        while (this.maxSize > 0 && added && size() > this.maxSize) {
            remove();
        }
        return added;
    }
}
