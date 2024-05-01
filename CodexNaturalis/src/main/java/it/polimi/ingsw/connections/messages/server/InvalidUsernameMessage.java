package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.client.ConnectionBridge;

public class InvalidUsernameMessage extends ServerToClientMessage {
    public InvalidUsernameMessage() {}

    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.invalidUsername();
    }
}
