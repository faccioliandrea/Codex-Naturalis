package it.polimi.ingsw.connections.messages.client;

import it.polimi.ingsw.connections.server.ConnectionBridge;

public class ChoosePrivateGoalMessage extends ClientToServerMessage {
    final private int index;

    public ChoosePrivateGoalMessage(String username, int index) {
        this.username = username;
        this.index = index;
    }

    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.choosePrivateGoal(this.username, this.index);
    }
}
