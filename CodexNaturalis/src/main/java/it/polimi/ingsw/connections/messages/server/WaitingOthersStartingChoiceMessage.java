package it.polimi.ingsw.connections.messages.server;

import it.polimi.ingsw.connections.client.ConnectionBridge;

/**
 * Message that notifies the client that it has to wait for the other players to choose their starting position
 */
public class WaitingOthersStartingChoiceMessage extends ServerToClientMessage{
    /**
     * Constructor
     */
    public WaitingOthersStartingChoiceMessage() {}

    /**
     * Execute a method on the client to notify that it has to wait for the other players to choose their starting position
     * @param bridge the client's bridge
     */
    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.WaitingOthersStartingChoiceMessage();
    }

}
