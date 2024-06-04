package it.polimi.ingsw.chat;

import it.polimi.ingsw.connections.client.ConnectionBridge;
import it.polimi.ingsw.controller.client.ClientController;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientChatHandler implements Runnable {
    private static ClientChatHandler instance;
    private static String username;

    private static final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private static final MessagesQueue msgsQueue = new MessagesQueue();
    private static final Pattern mention_pattern = Pattern.compile("@\\S+");
    private static boolean running = false;


    private ClientChatHandler() {
        username = ClientController.getInstance().getUsername();
    }

    public static synchronized ClientChatHandler getInstance() {
        if (instance == null) {
            instance = new ClientChatHandler();
        }
        return instance;
    }

    private static void sendChatMessage(ChatMessageData msg) {
        msgsQueue.add(msg);
        ClientController.getInstance().getData().setLastMessages(msgsQueue);
        ConnectionBridge.getInstance().sendChatMessage(msg);
    }

    private static ChatMessageData prepareMessage(String raw, String sender) {
        Matcher matcher = mention_pattern.matcher(raw);
        if (matcher.find()) {
            String recipient = matcher.group().substring(1);
            String contents = matcher.replaceAll("");
            return new ChatMessageData(sender, recipient, contents.trim());
        } else {
            return new ChatMessageData(sender, null, raw.trim());
        }
    }

    public static boolean isSameUserMessage(String raw) {
        String messageRecipient =  prepareMessage(raw, username).getRecipient();
        return messageRecipient != null && messageRecipient.equals(username);
    }

    public static void sendChatMessage(String raw) {
        sendChatMessage(prepareMessage(raw, username));
    }

    public static void recvChatMessage(ChatMessageData msg) {
        msgsQueue.add(msg);
        ClientController.getInstance().getData().setLastMessages(msgsQueue);
    }

    public static MessagesQueue getMsgsQueue() {
        return msgsQueue;
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                String msg = queue.take();
                sendChatMessage(msg);
            } catch (InterruptedException e) {
                running = false;
            }
        }

    }

    public static boolean isRunning() {
        return running;
    }
}
