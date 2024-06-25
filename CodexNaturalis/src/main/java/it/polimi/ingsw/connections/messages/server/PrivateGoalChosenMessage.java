package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.client.ConnectionBridge;

/**
 * Message that notifies the client that the private goal has been chosen
 */
public class PrivateGoalChosenMessage extends ServerToClientMessage {
    /**
     * Constructor
     */
    public PrivateGoalChosenMessage() {}


    /**
     * Execute a method on the client to notify that the private goal has been chosen
     * @param bridge the client's bridge
     */
    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.privateGoalChosen();
    }
}
