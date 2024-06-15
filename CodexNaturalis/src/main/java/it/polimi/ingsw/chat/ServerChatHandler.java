package it.polimi.ingsw.chat;

import it.polimi.ingsw.connections.ConnectionStatus;
import it.polimi.ingsw.connections.server.ConnectionBridge;

import java.util.List;

/**
 * Class that handles the chat messages on the server side
 */
public class ServerChatHandler {
    private final List<String> users;

    /**
     * Constructor
     * @param users: the list of users
     */
    public ServerChatHandler(List<String> users) {
        this.users = users;
    }

    /**
     * Distribute a message to the recipients
     * @param msg: the message to distribute
     */
    public void distributeMessage(ChatMessageData msg) {
        if (ConnectionBridge.getInstance().getConnections().get(msg.getRecipient())==null) {
            msg.setRecipient(null);
        }
        if (msg.getRecipient() != null) {
            ConnectionBridge.getInstance().sendChatMessage(msg, msg.getRecipient());
        } else {
            for (String u: this.users) {
                if (ConnectionBridge.getInstance().getConnections().get(u).getStatus().equals(ConnectionStatus.ONLINE) && !u.equals(msg.getSender()))
                    ConnectionBridge.getInstance().sendChatMessage(msg, u);
            }
        }
    }

}
