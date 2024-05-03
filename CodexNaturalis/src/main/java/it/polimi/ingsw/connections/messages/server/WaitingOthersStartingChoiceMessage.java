package it.polimi.ingsw.connections.messages.server;

import it.polimi.ingsw.connections.client.ConnectionBridge;

public class WaitingOthersStartingChoiceMessage extends ServerToClientMessage{
    public WaitingOthersStartingChoiceMessage() {}

    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.WaitingOthersStartingChoiceMessage();
    }

}
