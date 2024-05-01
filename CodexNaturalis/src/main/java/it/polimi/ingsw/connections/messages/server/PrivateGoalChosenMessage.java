package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.client.ConnectionBridge;

public class PrivateGoalChosenMessage extends ServerToClientMessage {
    public PrivateGoalChosenMessage() {}

    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.privateGoalChosen();
    }
}
