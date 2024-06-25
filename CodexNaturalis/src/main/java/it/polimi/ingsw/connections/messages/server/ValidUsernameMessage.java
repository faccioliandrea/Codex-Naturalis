package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.client.ConnectionBridge;

/**
 * Message that notifies the client that the username is valid
 */
public class ValidUsernameMessage extends ServerToClientMessage {
    /**
     * Constructor
     */
    public ValidUsernameMessage() {}

    /**
     * Execute a method on the client to notify that the username is valid
     * @param bridge the client's bridge
     */
    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.validUsername();
    }
}
