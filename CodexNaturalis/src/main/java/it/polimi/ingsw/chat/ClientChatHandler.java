package it.polimi.ingsw.chat;

import it.polimi.ingsw.connections.client.ConnectionBridge;
import it.polimi.ingsw.controller.client.ClientController;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is the handler for the chat messages on the client side.
 */
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

    /**
     * Singleton instance getter
     * @return the instance of the ClientChatHandler
     */
    public static synchronized ClientChatHandler getInstance() {
        if (instance == null) {
            instance = new ClientChatHandler();
        }
        return instance;
    }

    /**
     * Send a message to the server.
     * @param msg the message to send
     */
    private static void sendChatMessage(ChatMessageData msg) {
        if (!msg.getSender().equals(msg.getRecipient())) {
            // if the sender equals the recipient, the message will be added on receive (private message to itself, avoid duplicates)
            msgsQueue.add(msg);
        }
        ClientController.getInstance().getData().setLastMessages(msgsQueue);
        ConnectionBridge.getInstance().sendChatMessage(msg);
    }

    /**
     * Prepare a message to be sent.
     * @param raw the raw message
     * @param sender the sender of the message
     * @return the prepared message
     */
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

    /**
     * Check if a message is a message to the same user.
     * @param raw the raw message
     * @return true if the message is to the same user, false otherwise
     */
    public static boolean isSameUserMessage(String raw) {
        String messageRecipient =  prepareMessage(raw, username).getRecipient();
        return messageRecipient != null && messageRecipient.equals(username);
    }

    /**
     * Send a message to the server.
     * @param raw the raw message
     */
    public static void sendChatMessage(String raw) {
        sendChatMessage(prepareMessage(raw, username));
    }

    /**
     * Receive a message from the server.
     * @param msg the message received
     */
    public static void recvChatMessage(ChatMessageData msg) {
        msgsQueue.add(msg);
        ClientController.getInstance().getData().setLastMessages(msgsQueue);
    }

    /**
     * Getter for the messages queue.
     * @return the messages queue
     */
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

    /**
     * Getter for the running status of the ClientChatHandler.
     * @return true if the ClientChatHandler is running, false otherwise
     */
    public static boolean isRunning() {
        return running;
    }

    public static void  reset() {
        msgsQueue.clear();
    }
}
