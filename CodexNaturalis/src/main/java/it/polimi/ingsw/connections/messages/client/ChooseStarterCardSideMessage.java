package it.polimi.ingsw.connections.messages.client;

import it.polimi.ingsw.connections.server.ConnectionBridge;

public class ChooseStarterCardSideMessage extends ClientToServerMessage {
    private boolean flipped;

    public ChooseStarterCardSideMessage(String username, boolean flipped) {
        this.username = username;
        this.flipped = flipped;
    }

    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.chooseStarterCardSide(this.username, this.flipped);
    }
}
