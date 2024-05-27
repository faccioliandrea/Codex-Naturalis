package it.polimi.ingsw.connections.messages.server;

import it.polimi.ingsw.chat.ChatMessageData;
import it.polimi.ingsw.connections.client.ConnectionBridge;

public class ServerToClientChatMessage extends ServerToClientMessage {
    private final ChatMessageData msg;

    public ServerToClientChatMessage(ChatMessageData msg) {
        this.msg = msg;
    }

    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.recvChatMessage(msg);
    }
}
