package it.polimi.ingsw.chat;

import it.polimi.ingsw.connections.ConnectionStatus;
import it.polimi.ingsw.connections.server.ConnectionBridge;

import java.util.List;

public class ServerChatHandler {
    private final List<String> users;

    public ServerChatHandler(List<String> users) {
        this.users = users;
    }

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
