package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.client.ConnectionBridge;
import it.polimi.ingsw.connections.enums.LogInResponse;

public class InvalidUsernameMessage extends ServerToClientMessage {
    private final LogInResponse status;

    public InvalidUsernameMessage(LogInResponse status) {
        this.status = status;
    }

    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.invalidUsername(status);
    }
}
