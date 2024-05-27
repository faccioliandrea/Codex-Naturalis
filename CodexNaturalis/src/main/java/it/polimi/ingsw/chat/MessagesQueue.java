package it.polimi.ingsw.chat;

import java.util.LinkedList;

public class MessagesQueue extends LinkedList<ChatMessageData> {
    private final int maxSize;

    public MessagesQueue(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public synchronized boolean add(ChatMessageData o) {
        boolean added = super.add(o);
        while (added && size() > this.maxSize) {
            super.remove();
        }
        return added;
    }
}
