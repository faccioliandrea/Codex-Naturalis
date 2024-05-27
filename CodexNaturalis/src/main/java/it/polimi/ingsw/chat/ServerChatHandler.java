package it.polimi.ingsw.chat;

import it.polimi.ingsw.connections.ConnectionStatus;
import it.polimi.ingsw.connections.server.ConnectionBridge;

import java.util.List;

public class ServerChatHandler {
    private ConnectionBridge bridge;
    private List<String> users;

    public ServerChatHandler(ConnectionBridge bridge, List<String> users) {
        this.bridge = bridge;
        this.users = users;
    }

    public void distributeMessage(ChatMessageData msg) {
        if (bridge.getConnections().get(msg.getRecipient())==null) {
            msg.setRecipient(null);
        }
        if (msg.getRecipient() != null) {
            bridge.sendChatMessage(msg, msg.getRecipient());
        } else {
            for (String u: this.users) {
                if (bridge.getConnections().get(u).getStatus().equals(ConnectionStatus.ONLINE) && !u.equals(msg.getSender()))
                    bridge.sendChatMessage(msg, u);
            }
        }
    }

}
