package it.polimi.ingsw.connections.messages.client;

import it.polimi.ingsw.chat.ChatMessageData;
import it.polimi.ingsw.connections.server.ConnectionBridge;

public class ClientToServerChatMessage extends ClientToServerMessage {
    private final ChatMessageData msg;

    public ClientToServerChatMessage(ChatMessageData msg){
        this.msg = msg;
    }

    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.recvChatMessage(msg);
    }
}
