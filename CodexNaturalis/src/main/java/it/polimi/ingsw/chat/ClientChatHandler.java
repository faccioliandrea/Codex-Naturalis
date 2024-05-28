package it.polimi.ingsw.chat;

import it.polimi.ingsw.connections.client.ConnectionBridge;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientChatHandler implements Runnable{
    private final ConnectionBridge bridge;
    private final String username;

    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private final MessagesQueue msgsQueue = new MessagesQueue(5);
    private final Consumer<MessagesQueue> updater;
    private final Pattern mention_pattern;


    public ClientChatHandler(ConnectionBridge bridge, String username, Consumer<MessagesQueue> updater) {
        this.bridge = bridge;
        this.username = username;
        this.updater = updater;
        mention_pattern = Pattern.compile("@\\S+");
    }

    private void sendChatMessage(ChatMessageData msg) {
        msgsQueue.add(msg);
        updater.accept(msgsQueue);
        bridge.sendChatMessage(msg);
    }

    private ChatMessageData prepareMessage(String raw, String sender) {
        Matcher matcher = mention_pattern.matcher(raw);
        if (matcher.find()) {
            String recipient = matcher.group().substring(1);
            String contents = matcher.replaceAll("");
            return new ChatMessageData(sender, recipient, contents.trim());
        } else {
            return new ChatMessageData(sender, null, raw.trim());
        }
    }

    public void sendChatMessage(String raw) {
        sendChatMessage(prepareMessage(raw, username));
    }

    public void recvChatMessage(ChatMessageData msg) {
        msgsQueue.add(msg);
        this.updater.accept(this.msgsQueue);
    }

    public MessagesQueue getMsgsQueue() {
        return msgsQueue;
    }

    @Override
    public void run() {
        boolean running = true;
        while (running) {
            try {
                String msg = queue.take();
                sendChatMessage(msg);
            } catch (InterruptedException e) {
                running = false;
            }
        }

    }
}
