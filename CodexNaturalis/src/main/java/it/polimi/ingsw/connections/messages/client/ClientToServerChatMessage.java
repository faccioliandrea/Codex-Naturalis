package it.polimi.ingsw.connections.messages.client;

import it.polimi.ingsw.chat.ChatMessageData;
import it.polimi.ingsw.connections.server.ConnectionBridge;

/**
 * Message that contains a chat message
 */
public class ClientToServerChatMessage extends ClientToServerMessage {
    private final ChatMessageData msg;

    /**
     * Constructor
     * @param msg the chat message
     */
    public ClientToServerChatMessage(ChatMessageData msg){
        this.msg = msg;
    }

    /**
     * Execute a method on the server to receive a chat message
     * @param bridge the server's bridge
     */
    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.recvChatMessage(msg);
    }
}
