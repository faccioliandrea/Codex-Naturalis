package it.polimi.ingsw.connections.messages.server;

import it.polimi.ingsw.chat.ChatMessageData;
import it.polimi.ingsw.connections.client.ConnectionBridge;

/**
 * Message that notifies the client that a chat message has been received
 */
public class ServerToClientChatMessage extends ServerToClientMessage {
    private final ChatMessageData msg;

    /**
     * Constructor
     * @param msg the chat message
     */
    public ServerToClientChatMessage(ChatMessageData msg) {
        this.msg = msg;
    }

    /**
     * Execute a method on the client to notify that a chat message has been received
     * @param bridge the client's bridge
     */
    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.recvChatMessage(msg);
    }
}
