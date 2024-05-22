package it.polimi.ingsw.connections.messages.server;

import it.polimi.ingsw.connections.client.ConnectionBridge;

public class NoOtherPlayerConnectedMessage extends ServerToClientMessage{
    public NoOtherPlayerConnectedMessage() {
    }
    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.noOtherPlayerConnected();
    }
}
