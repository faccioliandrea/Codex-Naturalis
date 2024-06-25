package it.polimi.ingsw.connections.messages.server;

import it.polimi.ingsw.connections.client.ConnectionBridge;

/**
 * Message that notifies the client that no other player is connected
 */
public class NoOtherPlayerConnectedMessage extends ServerToClientMessage{

    /**
     * Constructor
     */
    public NoOtherPlayerConnectedMessage() {}

    /**
     * Execute a method on the client to notify that no other player is connected
     * @param bridge the client's bridge
     */
    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.noOtherPlayerConnected();
    }
}
