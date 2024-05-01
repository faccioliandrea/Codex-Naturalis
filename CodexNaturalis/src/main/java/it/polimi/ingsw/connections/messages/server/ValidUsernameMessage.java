package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.client.ConnectionBridge;

public class ValidUsernameMessage extends ServerToClientMessage {
    public ValidUsernameMessage() {}

    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.validUsername();
    }
}
